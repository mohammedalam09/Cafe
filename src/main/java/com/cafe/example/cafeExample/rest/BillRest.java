package com.cafe.example.cafeExample.rest;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/bill")
public interface BillRest {
	
	@PostMapping("/generateReport")
	ResponseEntity<?> generateReport(@RequestBody(required = true) Map<String, String> reportMap);
	
	@GetMapping("/getBill")
	ResponseEntity<?> getAllBills();
	
	@PostMapping("/getPDF")
	ResponseEntity<?> getPDF(@RequestBody(required = true) Map<String, String> pdfMap);
	
	

}
