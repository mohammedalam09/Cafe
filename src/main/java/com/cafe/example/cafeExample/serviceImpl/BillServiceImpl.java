package com.cafe.example.cafeExample.serviceImpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Map;
import java.util.stream.Stream;

import org.apache.pdfbox.io.IOUtils;
import org.json.JSONArray;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cafe.example.cafeExample.constant.CafeConstant;
import com.cafe.example.cafeExample.dao.BillDao;
import com.cafe.example.cafeExample.jwt.JwtFilter;
import com.cafe.example.cafeExample.pojo.Bill;
import com.cafe.example.cafeExample.service.BillService;
import com.cafe.example.cafeExample.utils.CafeUtils;
import com.cafe.example.cafeExample.utils.CheckIsNull;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

@Service
public class BillServiceImpl implements BillService {

	@Autowired
	private JwtFilter jwtFilter;

	@Autowired
	private BillDao billDao;

	@Override
	public ResponseEntity<?> generateReport(Map<String, String> reportMap) {

		try {
			String fileName;

			if (validateReportData(reportMap)) {
				if (reportMap.containsKey("isGenerate") && !Boolean.valueOf(reportMap.get("isGenerate"))) {
					fileName = reportMap.get("uuid");
				} else {
					fileName = CafeUtils.getUUId();
					reportMap.put("uuid", fileName);
					insertBill(reportMap);
				}
				String data = "Name:" + reportMap.get("name") + "\n" + "Contact Number:"
						+ reportMap.get("contactNumber") + "\n" + "Email:" + reportMap.get("email") + "\n"
						+ "Payment Method:" + reportMap.get("paymentMethod");

				Document doc = new Document();
				PdfWriter.getInstance(doc, new FileOutputStream(CafeConstant.PDF_LOCATION + "\\" + fileName + ".pdf"));
				doc.open();
				setRectanglePdf(doc);
				Paragraph par = new Paragraph("Cafe Management System", getFont("Header"));
				par.setAlignment(Element.ALIGN_CENTER);
				doc.add(par);
				Paragraph p = new Paragraph(data + "\n \n", getFont("data"));
				doc.add(p);
				PdfPTable table = new PdfPTable(5);
				table.setWidthPercentage(100);
				addTableHeader(table);
				JSONArray arr = CafeUtils.getJsonArrayFromString(reportMap.get("productDetails"));
				for (int i = 0; i < arr.length(); i++) {
					Map<String, Object> mapFromJSON = CafeUtils.getMapFromJSON(arr.get(i).toString());
					addRow(table, mapFromJSON);

				}
				doc.add(table);

				Paragraph footer = new Paragraph("Total: " + reportMap.get("totalAmount")+ " Thank You For Visiting us!:)",
						getFont("data"));
				doc.add(footer);
				doc.close();

				return new ResponseEntity<>("{\"uuid\":\"" + fileName + "\"}", HttpStatus.OK);
			} else {
				return CafeUtils.getResponseHandler(CafeConstant.INVALID_REQUEST, HttpStatus.BAD_REQUEST);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ResponseEntity<>(CafeConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	private void addRow(PdfPTable table, Map<String, Object> data) {

		table.addCell((String) data.get("Name"));
		table.addCell((String) data.get("Category"));
		table.addCell((String) data.get("Quantity"));
		table.addCell(data.get("Price").toString());
		table.addCell(data.get("Sub Total").toString());

	}

	private void addTableHeader(PdfPTable pdf) {
		Stream.of("Name", "Category", "Quantity", "Price", "Sub Total").forEach(c -> {
			PdfPCell header = new PdfPCell();
			header.setBackgroundColor(BaseColor.YELLOW);
			header.setBorderWidth(2);
			header.setPhrase(new Phrase(c));
			header.setHorizontalAlignment(Element.ALIGN_CENTER);
			header.setVerticalAlignment(Element.ALIGN_CENTER);
			pdf.addCell(header);
		});
	}

	private Font getFont(String type) {

		switch (type) {

		case "Header":
			Font header = FontFactory.getFont(FontFactory.HELVETICA_BOLDOBLIQUE, 18, BaseColor.RED);
			header.setStyle(Font.ITALIC);
			return header;
		case "data":
			Font data = FontFactory.getFont(FontFactory.TIMES_ROMAN, 18, BaseColor.RED);
			data.setStyle(Font.BOLD);
			return data;

		default:
			return new Font();
		}

	}

	private void setRectanglePdf(Document doc) throws DocumentException {
		Rectangle rect = new Rectangle(577, 875, 15, 18);
		rect.enableBorderSide(1);
		rect.enableBorderSide(2);
		rect.enableBorderSide(4);
		rect.enableBorderSide(8);
		rect.setBackgroundColor(BaseColor.WHITE);
		rect.setBorderWidth(1);
		doc.add(rect);
	}

	private void insertBill(Map<String, String> reportMap) {
		try {
			Bill bill = new Bill();
			bill.setUuid(reportMap.get("uuid"));
			bill.setName(reportMap.get("name"));
			bill.setEmail(reportMap.get("email"));
			bill.setContactNumber(reportMap.get("contactNumber"));
			bill.setPaymentMethod(reportMap.get("paymentMethod"));
			bill.setTotal(Integer.parseInt(reportMap.get("totalAmount")));
			bill.setProductDetails(reportMap.get("productDetails"));
			bill.setCreatedBy(jwtFilter.getCurrentUser());

			billDao.save(bill);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private boolean validateReportData(Map<String, String> reportMap) {

		return reportMap.containsKey("name") && reportMap.containsKey("contactNumber") && reportMap.containsKey("email")
				&& reportMap.containsKey("paymentMethod") && reportMap.containsKey("productDetails")
				&& reportMap.containsKey("totalAmount");
	}

	@Override
	public ResponseEntity<?> getAllBills() {

		try {
			if (jwtFilter.isAdmin()) {
				return new ResponseEntity<>(billDao.getAllBills(), HttpStatus.OK);
			} else {
				return new ResponseEntity<>(billDao.getBillByCurrentUser(jwtFilter.getCurrentUser()), HttpStatus.OK);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ResponseEntity<>(CafeConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);

	}

	@Override
	public ResponseEntity<?> getPDF(Map<String, String> pdfMap) {

		byte[] pdfArr = new byte[0];

		if (!pdfMap.containsKey("uuid") && validateReportData(pdfMap)) {
			return new ResponseEntity<>(pdfArr, HttpStatus.BAD_REQUEST);

		}
		try {
			String filePath = CafeConstant.PDF_LOCATION + "\\" + CheckIsNull.forString(pdfMap.get("uuid")) + ".pdf";
			if (CafeUtils.isFileExist(filePath)) {

				pdfArr = getByteArray(filePath);

				return new ResponseEntity<>(pdfArr, HttpStatus.OK);
			} else {
				pdfMap.put("isGenerate", "false");
				
				generateReport(pdfMap);

				pdfArr = getByteArray(filePath);

				return new ResponseEntity<>(pdfArr, HttpStatus.OK);
			}
		} catch (Exception e) {

			e.printStackTrace();
		}

		return null;
	}

	private byte[] getByteArray(String filePath) throws Exception {
		
		File file = new File(filePath);
		InputStream in = new FileInputStream(file);
		byte[] b = IOUtils.toByteArray(in);
		return b;

	}


}
