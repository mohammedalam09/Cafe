package com.cafe.example.cafeExample.restImpl;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.cafe.example.cafeExample.constant.CafeConstant;
import com.cafe.example.cafeExample.rest.ProductRest;
import com.cafe.example.cafeExample.service.ProductService;
import com.cafe.example.cafeExample.wrapper.ProductWrapper;

@RestController
public class ProductRestImpl implements ProductRest {

	@Autowired
	private ProductService productService;

	@Override
	public ResponseEntity<?> addProduct(Map<String, String> productMap) {
		try {
			return productService.addNewProduct(productMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(CafeConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);

	}

	@Override
	public ResponseEntity<List<ProductWrapper>> getAllProducts() {
		try {
			return productService.getAllProducts();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(Collections.emptyList(), HttpStatus.NO_CONTENT);

	}

	@Override
	public ResponseEntity<?> updateProduct(Map<String, String> productUpdateMap) {
		try {
			return productService.updateProduct(productUpdateMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(CafeConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<?> deleteProduct(String id) {
		try {
			return productService.deleteProduct(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(CafeConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<?> updateProductStatus(Map<String, String> productUpdateStatusMap) {
		try {
			return productService.updateProductStatus(productUpdateStatusMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(CafeConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<?> getProductByCategory(String id) {
		try {
			return productService.getProductByCategory(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(CafeConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<?> getProductById(String id) {
		try {
			return productService.getProductById(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(CafeConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
