package com.cafe.example.cafeExample.restImpl;

import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.cafe.example.cafeExample.constant.CafeConstant;
import com.cafe.example.cafeExample.rest.CategoryRest;
import com.cafe.example.cafeExample.service.CategoryService;

@RestController
public class CategoryRestImpl implements CategoryRest {

	@Autowired
	private CategoryService categoryService;

	@Override
	public ResponseEntity<?> addCategory(Map<String, String> categoryMap) {
		try {
			return categoryService.addNewCategory(categoryMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(CafeConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<?> getAllCategory(String filterValue) {
		try {
			return categoryService.getAllCategory(filterValue);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(Arrays.asList(), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> updateCategory(Map<String, String> categoryMap) {
		try {
			return categoryService.updateCategory(categoryMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(Arrays.asList(), HttpStatus.OK);
	}

}
