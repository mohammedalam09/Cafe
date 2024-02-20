package com.cafe.example.cafeExample.rest;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cafe.example.cafeExample.wrapper.ProductWrapper;

@RequestMapping("/product")
public interface ProductRest {

	@PostMapping(path = "/add")
	ResponseEntity<?> addProduct(@RequestBody(required = true) Map<String, String> productMap);

	@GetMapping(path = "/")
	ResponseEntity<List<ProductWrapper>> getAllProducts();

	@PutMapping(path = "/update")
	ResponseEntity<?> updateProduct(@RequestBody(required = true) Map<String, String> productUpdateMap);

	@DeleteMapping(path = "delete/{id}")
	ResponseEntity<?> deleteProduct(@PathVariable String id);

	@PutMapping(path = "/updateProductStatus")
	ResponseEntity<?> updateProductStatus(@RequestBody(required = true) Map<String, String> productUpdateStatusMap);
	
	@GetMapping(path = "/getByCategory/{catId}")
	ResponseEntity<?> getProductByCategory(@PathVariable("catId") String id);
	
	@GetMapping(path = "/getProductById/{pId}")
	ResponseEntity<?> getProductById(@PathVariable("pId") String id);
	
	
}
