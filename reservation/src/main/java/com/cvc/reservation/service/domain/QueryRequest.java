package com.cvc.reservation.service.domain;

import java.text.SimpleDateFormat;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class QueryRequest {

	Integer cityCode;
	Date checkin;
	Date checkout;
	Integer adult;
	Integer child;

	public String url() {
		return "/rest/".concat(String.valueOf(cityCode)).concat("/")
				.concat(new SimpleDateFormat("yyyy-MM-dd").format(checkin)).concat("/")
				.concat(new SimpleDateFormat("yyyy-MM-dd").format(checkout)).concat("/")
				.concat(String.valueOf(adult)).concat("/")
				.concat(String.valueOf(child)).concat("/");
	}
}
