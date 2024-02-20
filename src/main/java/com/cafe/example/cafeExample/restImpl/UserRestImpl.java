package com.cafe.example.cafeExample.restImpl;

import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.cafe.example.cafeExample.rest.UserRest;
import com.cafe.example.cafeExample.service.UserService;
import com.cafe.example.cafeExample.utils.CafeUtils;

@RestController
public class UserRestImpl implements UserRest {
	

	@Autowired
	UserService userService;

	@Override
	public ResponseEntity<?> signUp(Map<String, String> signupReq) {
		try {
			return userService.signup(signupReq);
		} catch (Exception e) {
			System.out.println(e.getMessage());

			return CafeUtils.getResponseHandler("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@Override
	public ResponseEntity<?> login(Map<String, String> loginRequest) {
		try {
			return userService.login(loginRequest);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return CafeUtils.getResponseHandler("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<?> getResp() {
		return new ResponseEntity<>("{\" hello \"}", HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> getAllUsers() {
		try {
			return userService.getAllUsers();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(Collections.EMPTY_LIST, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<?> update(Map<String, String> updateReqMap) {

		try {
			return userService.update(updateReqMap);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return CafeUtils.getResponseHandler("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<?> checkToken() {
		try {
			return userService.checkToken();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return CafeUtils.getResponseHandler("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<?> forgetPassword(Map<String, String> forgetpasswordReqMap) {
		try {
			return userService.forgetPassword(forgetpasswordReqMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return CafeUtils.getResponseHandler("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<?> changePassword(Map<String, String> changePasswordReqMap) {
		try {
			return userService.changePassword(changePasswordReqMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return CafeUtils.getResponseHandler("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
