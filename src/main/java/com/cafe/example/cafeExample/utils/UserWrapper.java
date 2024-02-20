package com.cafe.example.cafeExample.utils;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserWrapper {

	private Integer id;
	private String contactNumber;
	private String name;
	private String email;
//	private String role;
//	private String password;
	private String status;
	
	public UserWrapper(Integer id, String contactNumber, String name, String email, String status) {
		super();
		this.id = id;
		this.contactNumber = contactNumber;
		this.name = name;
		this.email = email;
		this.status = status;
	}
	
	
	
}
