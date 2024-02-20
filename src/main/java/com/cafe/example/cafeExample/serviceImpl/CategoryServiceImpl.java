package com.cafe.example.cafeExample.serviceImpl;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cafe.example.cafeExample.constant.CafeConstant;
import com.cafe.example.cafeExample.dao.CategoryDao;
import com.cafe.example.cafeExample.jwt.JwtFilter;
import com.cafe.example.cafeExample.pojo.Category;
import com.cafe.example.cafeExample.service.CategoryService;
import com.cafe.example.cafeExample.utils.CafeUtils;
import com.google.common.base.Strings;

@Service
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	private JwtFilter jwtfilter;

	@Autowired
	private CategoryDao categoryDao;

	@Override
	public ResponseEntity<?> addNewCategory(Map<String, String> requestMap) {

		try {
			if (jwtfilter.isAdmin()) {

				if (validateCategory(requestMap, false)) {

					categoryDao.save(getCategoryFromMap(requestMap, false));

					return CafeUtils.getResponseHandler("Category Added Successfully!!!", HttpStatus.OK);
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

	private boolean validateCategory(Map<String, String> requestMap, boolean validateId) {

		if (requestMap.containsKey("name")) {
			if (requestMap.containsKey("id") && validateId) {
				return true;
			} else if (!validateId) {
				return true;
			}
		}

		return false;
	}

	@SuppressWarnings("unused")
	private Category getCategoryFromMap(Map<String, String> requestMap, boolean isAdd) {
		Category category = new Category();
		if (isAdd) {
			category.setId(Integer.parseInt(requestMap.get("id")));
		}
		category.setName(requestMap.get("name"));

		return category;

	}

	@Override
	public ResponseEntity<?> getAllCategory(String filterValue) {
		if (!Strings.isNullOrEmpty(filterValue) && Boolean.parseBoolean(filterValue)) {
			return new ResponseEntity<>(categoryDao.getAllCategories(), HttpStatus.OK);
		}
		return new ResponseEntity<>(categoryDao.findAll(), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> updateCategory(Map<String, String> categoryMap) {

		try {
			if (jwtfilter.isAdmin()) {
				if (validateCategory(categoryMap, true)) {

					 Optional<Category> category = categoryDao.findById(Integer.parseInt(categoryMap.get("id")));

					if (category.isPresent()) {

						categoryDao.save(getCategoryFromMap(categoryMap, true));

						return CafeUtils.getResponseHandler("Category Updated Successfully!!!", HttpStatus.OK);
					} else {
						return CafeUtils.getResponseHandler(CafeConstant.RESOURCE_DOES_NOT_EXIST,
								HttpStatus.PRECONDITION_FAILED);
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

}
