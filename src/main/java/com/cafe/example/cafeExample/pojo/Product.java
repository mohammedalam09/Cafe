package com.cafe.example.cafeExample.pojo;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import lombok.Data;

@NamedQuery(name = "Product.getAllProducts", query = "SELECT new com.cafe.example.cafeExample.wrapper.ProductWrapper(p.id,p.name,p.description,p.price,p.category.id,p.category.name,p.status) FROM Product p")
@NamedQuery(name = "Product.updateProductStatus", query = "UPDATE Product p SET p.status=:status WHERE p.id=:id")
@NamedQuery(name = "Product.getProductByCategory", query = "SELECT new com.cafe.example.cafeExample.wrapper.ProductWrapper(p.id,p.name)  FROM Product p WHERE p.category.id=:id and p.status='true'")
@Data
@Entity
@Table(name = "PRODUCT")
@DynamicInsert
@DynamicUpdate
public class Product implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String name;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_fk", nullable = false)
	private Category category;
	private String description;
	private Integer price;
	private String status;

}
