package com.cvc.HotelDetails.service.domain;

import java.text.SimpleDateFormat;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class QueryRequest {

	Integer id;
    Date checkin;
	Date checkout;
	Integer adult;
	Integer child;

	public String url() {
		return "/rest/".concat(String.valueOf(id)).concat("/")
				.concat(new SimpleDateFormat("yyyy-MM-dd").format(checkin)).concat("/")
				.concat(new SimpleDateFormat("yyyy-MM-dd").format(checkout)).concat("/")
				.concat(String.valueOf(adult)).concat("/")
				.concat(String.valueOf(child)).concat("/");
	}

}
