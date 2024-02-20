package com.cafe.example.cafeExample.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.cafe.example.cafeExample.constant.CafeConstant;
import com.cafe.example.cafeExample.dao.UserDao;
import com.cafe.example.cafeExample.jwt.CustomUserDetailsService;
import com.cafe.example.cafeExample.jwt.JwtFilter;
import com.cafe.example.cafeExample.jwt.JwtUtil;
import com.cafe.example.cafeExample.pojo.User;
import com.cafe.example.cafeExample.service.UserService;
import com.cafe.example.cafeExample.utils.CafeUtils;
import com.cafe.example.cafeExample.utils.EmailUtils;
import com.google.common.base.Strings;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

	@Autowired
	UserDao userDao;

	@Autowired
	AuthenticationManager authManager;

	@Autowired
	CustomUserDetailsService customUserDetailsService;

	@Autowired
	JwtUtil jwtUtil;

	@Autowired
	JwtFilter jwtfilter;

	@Autowired
	EmailUtils emailUtils;

	@Override
	public ResponseEntity<?> signup(Map<String, String> signupreq) {
		log.info("Inside SignUp Service");
		if (validateSignupReq(signupreq)) {
			User findByEmail = userDao.findByEmail(signupreq.get("email"));
			if (Objects.isNull(findByEmail)) {
				userDao.save(getUserFromMap(signupreq));
				return CafeUtils.getResponseHandler(CafeConstant.USER_REGISTERED_SUCCESSFULLY, HttpStatus.OK);
			} else {
				return CafeUtils.getResponseHandler(CafeConstant.USER_ALREADY_REGISTERED,
						HttpStatus.PRECONDITION_FAILED);
			}
		} else {
			return CafeUtils.getResponseHandler(CafeConstant.INVALID_USER_INFO, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	private boolean validateSignupReq(Map<String, String> signupreq) {
		// validate signup request
		if (signupreq.containsKey("contactNumber") && signupreq.containsKey("password")
				&& signupreq.containsKey("email") && signupreq.containsKey("name")) {
			return true;
		} else {
			return false;
		}

	}

	private User getUserFromMap(Map<String, String> signupreq) {

		User user = new User();
		user.setContactNumber(signupreq.get("contactNumber"));
		user.setEmail(signupreq.get("email"));
		user.setName(signupreq.get("name"));
		user.setPassword(signupreq.get("password"));
		user.setRole(signupreq.get("role"));
		user.setStatus("false");

		return user;

	}

	@Override
	public ResponseEntity<?> login(Map<String, String> loginReq) {
		log.info("Inside Login Service"); 
		try {
			Authentication auth = authManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginReq.get("email"), loginReq.get("password")));
			if (auth.isAuthenticated()) {
				if (customUserDetailsService.getUserDetail().getStatus().equalsIgnoreCase("true")) {
					return new ResponseEntity<>("{\"token\": \""
							+ jwtUtil.generateToken(customUserDetailsService.getUserDetail().getEmail(),
									customUserDetailsService.getUserDetail().getRole())
							+ "\"}", HttpStatus.OK);

				} else {
					return new ResponseEntity<>("{\"message\": \"" + "Wait for Admin approval" + "\"}",
							HttpStatus.BAD_REQUEST);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ResponseEntity<>("{\"message\": \"" + "Bad Credentials" + "\"}", HttpStatus.BAD_REQUEST);
	}

	@Override
	public ResponseEntity<?> getAllUsers() {

		try {
			if (jwtfilter.isAdmin()) {

				return new ResponseEntity<>(userDao.getAllUsers(), HttpStatus.OK);
			} else {
				return new ResponseEntity<>(new ArrayList<>(), HttpStatus.UNAUTHORIZED);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<?> update(Map<String, String> updateReqMap) {

		try {

			if (jwtfilter.isAdmin()) {

				Optional<User> optional = userDao.findById(Integer.parseInt(updateReqMap.get("id")));

				if (optional.isPresent()) {
					userDao.updateStatus(updateReqMap.get("status"), Integer.parseInt(updateReqMap.get("id")));

					sendMailToAllAdmin(updateReqMap.get("status"), optional.get().getEmail(), userDao.getAllAdmin());

					return CafeUtils.getResponseHandler("User status updated successfully!!!", HttpStatus.OK);
				} else {
					return CafeUtils.getResponseHandler("User id does not exist", HttpStatus.OK);
				}

			} else {
				return CafeUtils.getResponseHandler(CafeConstant.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	private void sendMailToAllAdmin(String status, String user, List<String> allAdmin) {

		allAdmin.remove(jwtfilter.getCurrentUser());
		if (null != status && Boolean.getBoolean(status)) {
			emailUtils.sendSimpleMessage(jwtfilter.getCurrentUser(), "Account Approved",
					"User:-" + user + "\n is approved by  \nADMIN :-" + jwtfilter.getCurrentUser(), allAdmin);
		} else {
			emailUtils.sendSimpleMessage(jwtfilter.getCurrentUser(), "Account Disabled",
					"User:-" + user + "\n is approved by  \nADMIN :-" + jwtfilter.getCurrentUser(), allAdmin);
		}

	}

	@Override
	public ResponseEntity<?> checkToken() {

		return CafeUtils.getResponseHandler(Boolean.TRUE.toString(), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> changePassword(Map<String, String> changePasswordReqMap) {

		User user = userDao.findByEmail(jwtfilter.getCurrentUser());

		if (!Objects.isNull(user)) {
			if (user.getPassword().equalsIgnoreCase(changePasswordReqMap.get("oldPassword"))) {

				user.setPassword(changePasswordReqMap.get("newPassword"));
				userDao.save(user);

				return CafeUtils.getResponseHandler("Password Updated Successfully", HttpStatus.OK);
			}
			return CafeUtils.getResponseHandler("Incorrect Old Password", HttpStatus.PRECONDITION_FAILED);
		}

		return CafeUtils.getResponseHandler(CafeConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<?> forgetPassword(Map<String, String> forgetpasswordReqMap) {
		try {

			User user = userDao.findByEmail(forgetpasswordReqMap.get("email"));
			if (!Objects.isNull(user) && !Strings.isNullOrEmpty(user.getEmail())) 
				emailUtils.forgetMail(user.getEmail(), "Password Reset", user.getPassword());
				
			return CafeUtils.getResponseHandler( "Check your mail for credentials", HttpStatus.OK);
			

		} catch (Exception e) {
			e.printStackTrace();
		}
		return CafeUtils.getResponseHandler(CafeConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
