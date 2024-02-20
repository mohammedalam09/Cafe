package com.cafe.example.cafeExample.service;

import java.util.Map;

import org.springframework.http.ResponseEntity;

public interface UserService {

	public ResponseEntity<?> signup(Map<String, String> signupReq);

	public ResponseEntity<?> login(Map<String, String> loginReq);

	public ResponseEntity<?> getAllUsers();

	public ResponseEntity<?> update(Map<String, String> updateReqMap);
	
	public ResponseEntity<?> checkToken();
	
	public ResponseEntity<?> changePassword(Map<String, String> changePasswordReqMap);
	
	public ResponseEntity<?> forgetPassword(Map<String, String> forgetpasswordReqMap);
}
