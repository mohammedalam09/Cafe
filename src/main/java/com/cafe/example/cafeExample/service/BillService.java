package com.cafe.example.cafeExample.service;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

public interface BillService {

	ResponseEntity<?> generateReport(@RequestBody(required = true) Map<String, String> reportMap);

	ResponseEntity<?> getAllBills();
	
	ResponseEntity<?> getPDF(@RequestBody(required = true) Map<String, String> pdfMap);

}
