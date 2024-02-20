package com.cafe.example.cafeExample.restImpl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cafe.example.cafeExample.constant.CafeConstant;
import com.cafe.example.cafeExample.rest.BillRest;
import com.cafe.example.cafeExample.service.BillService;

@RestController
public class BillRestImpl implements BillRest {
	
	@Autowired
	private BillService billService;

	@Override
	public ResponseEntity<?> generateReport(Map<String, String> reportMap) {
		try {
			return billService.generateReport(reportMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(CafeConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<?> getAllBills() {
		try {
			return billService.getAllBills();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(CafeConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<?> getPDF(@RequestBody(required = true) Map<String, String> pdfMap) {
		try {
			return billService.getPDF(pdfMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(CafeConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
