package com.cafe.example.cafeExample.newrelic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class Test {

	private static final Logger logger = LoggerFactory.getLogger(Controller.class);

	@GetMapping("/employee")
	public ResponseEntity<?> getEmployee() {
		Employee emp = new Employee();

		emp.setEmployeeName("Test Employee");
		emp.setEmployeeId("Emp001");
		emp.setEmployeeAddress("Rajasthan");

		logger.info("Employee ID: emp[{}]", emp.getEmployeeId());
		logger.info("Employee NAME: empNAME[{}]", emp.getEmployeeName());

		System.out.println("empid" + emp.getEmployeeId());
		System.out.println("empname" + emp.getEmployeeName());
		System.out.println("empaddress" + emp.getEmployeeAddress());

		return new ResponseEntity<>(emp, HttpStatus.OK);

	}

}
