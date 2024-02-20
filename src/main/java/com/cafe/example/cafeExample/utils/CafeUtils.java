package com.cafe.example.cafeExample.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.google.common.base.Strings;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

public class CafeUtils {

	public static ResponseEntity<String> getResponseHandler(String responseMessage, HttpStatus status) {
		return new ResponseEntity<String>("{\"message\":\"" + responseMessage + "\"}", status);

	}

	public static String getUUId() {
		Date date = new Date();
		long timeInMilis = date.getTime();
		return "Bill-" + timeInMilis;

	}

	public static JSONArray getJsonArrayFromString(String data) throws JSONException {
		return new JSONArray(data);
	}

	@SuppressWarnings("serial")
	public static Map<String, Object> getMapFromJSON(String json) {

		if (!Strings.isNullOrEmpty(json)) {
			return new Gson().fromJson(json, new TypeToken<Map<String, Object>>() {

			}.getType());
		}

		return new HashMap<String, Object>();

	}
	
	public static boolean isFileExist(String filePath) throws FileNotFoundException {
		File file = new File(filePath);
		return null != file && file.exists() ? true : false;
	}
}
