package com.cafe.example.cafeExample.pojo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import lombok.Data;

@NamedQuery( name = "Bill.getBillByCurrentUser",query = "select b from Bill b Where  b.createdBy=:name order by b.id desc")
@NamedQuery( name = "Bill.getAllBills",query = "select b from Bill b order by b.id desc" )
@Data
@Entity
@Table(name = "Bill")
@DynamicInsert
@DynamicUpdate
public class Bill implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String name;
	private String uuid;
	private String email;
	private String contactNumber;
	private String paymentMethod;
	private Integer total;
	@Column(columnDefinition = "json")
	private String productDetails;
	private String createdBy;

}
