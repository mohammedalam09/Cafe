package com.cafe.example.cafeExample.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cafe.example.cafeExample.pojo.Category;

public interface CategoryDao extends JpaRepository<Category, Integer> {

	List<Category> getAllCategories();
}
