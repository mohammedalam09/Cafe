package com.cafe.example.cafeExample.service;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

public interface CategoryService {
	
	ResponseEntity<?> addNewCategory(@RequestBody(required = true) Map<String, String> categoryMap);
	
	ResponseEntity<?> getAllCategory(String filterValue);
	
	ResponseEntity<?> updateCategory(Map<String, String> categoryMap);

}
