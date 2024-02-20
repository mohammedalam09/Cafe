package com.cafe.example.cafeExample.rest;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/category")
public interface CategoryRest {

	@PostMapping("/add")
	ResponseEntity<?> addCategory(@RequestBody(required = true) Map<String, String> categoryMap);

	@GetMapping("/")
	ResponseEntity<?> getAllCategory(@RequestParam(required = false,name = "filterValue") String filterValue);
	
	@PutMapping("/update")
	ResponseEntity<?> updateCategory(@RequestBody(required = true) Map<String, String> categoryMap);
}
