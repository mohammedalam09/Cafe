package com.cafe.example.cafeExample.service;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import com.cafe.example.cafeExample.wrapper.ProductWrapper;

public interface ProductService {
	
	ResponseEntity<?> addNewProduct(@RequestBody(required = true) Map<String, String> productMap);
	
	ResponseEntity<List<ProductWrapper>> getAllProducts();
	
	ResponseEntity<?> updateProduct(@RequestBody(required = true) Map<String, String> productUpdateMap);
	
	ResponseEntity<?> deleteProduct(String id);
	
	ResponseEntity<?> updateProductStatus(@RequestBody(required = true) Map<String, String>  productStatusUpdateMap);
	
	ResponseEntity<?> getProductByCategory(String id);
	
	ResponseEntity<?> getProductById(String id);

}
