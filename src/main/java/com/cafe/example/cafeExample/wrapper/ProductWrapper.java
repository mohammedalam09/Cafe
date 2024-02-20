package com.cafe.example.cafeExample.wrapper;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(value = Include.NON_NULL)
public class ProductWrapper {
	private Integer id;
	private String name;
	private String description;
	private Integer price;
	private Integer categoryId;
	private String categoryName;
	private String status;

	public ProductWrapper() {

	}

	public ProductWrapper(Integer id, String name, String description, Integer price, Integer categoryId,
			String categoryName, String status) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.price = price;
		this.categoryId = categoryId;
		this.categoryName = categoryName;
		this.status = status;
	}

	public ProductWrapper(Integer id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	
}
