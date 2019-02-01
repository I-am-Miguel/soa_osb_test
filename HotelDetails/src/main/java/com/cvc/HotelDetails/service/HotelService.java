package com.cvc.HotelDetails.service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.cvc.HotelDetails.domain.Hotel;
import com.cvc.HotelDetails.service.domain.HotelResponse;
import com.cvc.HotelDetails.service.domain.QueryRequest;

@Service
public class HotelService {

	@Autowired
	private RestTemplate restTemplate;

	@Value("${detailUrl}")
	private String urlRequest;

	public HotelResponse hotelDetails(QueryRequest hotelRequest) {
		ResponseEntity<Hotel[]> hotel = restTemplate.getForEntity(urlRequest, Hotel[].class, hotelRequest.getId());
		return HotelParser.hotelToResponse(hotel.getBody()[0], 
				LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(hotelRequest.getCheckin())),
				LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(hotelRequest.getCheckout())), 
				hotelRequest.getAdult(), hotelRequest.getChild());
	}

}
