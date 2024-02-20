package com.cafe.example.cafeExample.pojo;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import lombok.Data;

@NamedQuery(name = "Category.getAllCategories", query = "SELECT c from Category c WHERE  c.id IN (SELECT p.category FROM Product p WHERE p.status='true')")
@Data
@Entity
@Table(name = "CATEGORY")
@DynamicInsert
@DynamicUpdate
public class Category implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String name;

}
