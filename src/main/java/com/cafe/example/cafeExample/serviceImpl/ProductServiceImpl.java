package com.cafe.example.cafeExample.serviceImpl;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cafe.example.cafeExample.constant.CafeConstant;
import com.cafe.example.cafeExample.dao.ProductDao;
import com.cafe.example.cafeExample.jwt.JwtFilter;
import com.cafe.example.cafeExample.pojo.Category;
import com.cafe.example.cafeExample.pojo.Product;
import com.cafe.example.cafeExample.service.ProductService;
import com.cafe.example.cafeExample.utils.CafeUtils;
import com.cafe.example.cafeExample.wrapper.ProductWrapper;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private JwtFilter jwtfilter;

	@Autowired
	private ProductDao productDao;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public ResponseEntity<?> addNewProduct(Map<String, String> productMap) {
		try {

			if (jwtfilter.isAdmin()) {

				if (validateProduct(productMap, false)) {

					productDao.save(getProductFromMap(productMap, false));

					return CafeUtils.getResponseHandler("Product Added Successfully!!!", HttpStatus.OK);
				} else {
					return CafeUtils.getResponseHandler(CafeConstant.INVALID_REQUEST, HttpStatus.PRECONDITION_FAILED);
				}

			} else {
				return CafeUtils.getResponseHandler(CafeConstant.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return CafeUtils.getResponseHandler(CafeConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	private Product getProductFromMap(Map<String, String> productMap, boolean isAdd) {
		Category category = new Category();

		category.setId(Integer.parseInt(productMap.get("categoryId")));
		Product product = new Product();
		if (isAdd) {
			product.setId(Integer.parseInt(productMap.get("id")));
		} else {
			product.setStatus("true");
		}
		product.setPrice(Integer.parseInt(productMap.get("price")));
		product.setName(productMap.get("name"));
		product.setDescription(productMap.get("description"));
		product.setCategory(category);
		return product;

	}

	private boolean validateProduct(Map<String, String> productMap, Boolean validateId) {

		if (productMap.containsKey("name")) {
			if (validateId && productMap.containsKey("id")) {
				return true;

			} else if (!validateId) {
				return true;
			}
		}

		return false;
	}

	@Override
	public ResponseEntity<List<ProductWrapper>> getAllProducts() {
		try {

			return new ResponseEntity<List<ProductWrapper>>(productDao.getAllProducts(), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ResponseEntity<>(Collections.emptyList(), HttpStatus.NO_CONTENT);
	}

	@Override
	public ResponseEntity<?> updateProduct(Map<String, String> productUpdateMap) {
		try {

			if (jwtfilter.isAdmin()) {

				if (validateProduct(productUpdateMap, true)) {

					Optional<Product> product = productDao.findById(Integer.parseInt(productUpdateMap.get("id")));

					if (product.isPresent()) {
						Product productFromMap = getProductFromMap(productUpdateMap, true);
						productFromMap.setStatus(product.get().getStatus());
						productDao.save(productFromMap);
						return CafeUtils.getResponseHandler("Product Updated Successfully!!!", HttpStatus.OK);
					} else {
						return CafeUtils.getResponseHandler("Product Not Found", HttpStatus.PRECONDITION_FAILED);
					}

				} else {
					return CafeUtils.getResponseHandler(CafeConstant.INVALID_REQUEST, HttpStatus.PRECONDITION_FAILED);
				}

			} else {
				return CafeUtils.getResponseHandler(CafeConstant.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return CafeUtils.getResponseHandler(CafeConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<?> deleteProduct(String id) {
		try {

			if (jwtfilter.isAdmin()) {
				Optional<Product> product = productDao.findById(Integer.parseInt(id));
				if (product.isPresent()) {

					productDao.deleteById(Integer.parseInt(id));
					return CafeUtils.getResponseHandler("Product Deleted Successfully!!!", HttpStatus.OK);
				} else {
					return CafeUtils.getResponseHandler("Product Not Found", HttpStatus.PRECONDITION_FAILED);
				}

			} else {
				return CafeUtils.getResponseHandler(CafeConstant.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return CafeUtils.getResponseHandler(CafeConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<?> updateProductStatus(Map<String, String> productStatusUpdateMap) {
		try {

			if (jwtfilter.isAdmin()) {
				Optional<Product> product = productDao.findById(Integer.parseInt(productStatusUpdateMap.get("id")));
				if (product.isPresent()) {

					Integer i = productDao.updateProductStatus(productStatusUpdateMap.get("status"),
							Integer.parseInt(productStatusUpdateMap.get("id")));

					if (i > 0) {
						return CafeUtils.getResponseHandler("Product Status Updated Successfully!!!", HttpStatus.OK);
					} else {
						return CafeUtils.getResponseHandler("Product Status Updation Failed!!!",
								HttpStatus.INTERNAL_SERVER_ERROR);
					}

				} else {
					return CafeUtils.getResponseHandler("Product Not Found", HttpStatus.PRECONDITION_FAILED);
				}

			} else {
				return CafeUtils.getResponseHandler(CafeConstant.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return CafeUtils.getResponseHandler(CafeConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<?> getProductByCategory(String id) {
		try {

			if (jwtfilter.isAdmin()) {

				return ResponseEntity.ok(productDao.getProductByCategory(Integer.parseInt(id)));

			} else {
				return CafeUtils.getResponseHandler(CafeConstant.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return CafeUtils.getResponseHandler(CafeConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<?> getProductById(String id) {
		try {

			if (jwtfilter.isAdmin()) {
				Optional<Product> productById = productDao.findById(Integer.parseInt(id));
				if (productById.isPresent()) {

					return ResponseEntity.ok(this.modelMapper.map(productById.get(), ProductWrapper.class));
				}

				return CafeUtils.getResponseHandler("Product Not Found", HttpStatus.PRECONDITION_FAILED);

			} else {
				return CafeUtils.getResponseHandler(CafeConstant.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return CafeUtils.getResponseHandler(CafeConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
