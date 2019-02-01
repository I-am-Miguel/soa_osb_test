package com.cvc.HotelDetails.resource;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cvc.HotelDetails.service.HotelService;
import com.cvc.HotelDetails.service.domain.HotelResponse;
import com.cvc.HotelDetails.service.domain.QueryRequest;

import io.swagger.annotations.Api;

@RestController
@RequestMapping(value = "/rest", produces = "application/json")
@Api(tags = "Hotel Details")
public class HotelController {

	@Autowired
	private HotelService hotelService;

	@RequestMapping(method = RequestMethod.GET, value = "/details/{hotelId}/{checkin}/{checkout}/{adult_qtd}/{child_qtd}/")
	public ResponseEntity<HotelResponse> hotelDetails(@PathVariable("hotelId") Integer hotelId, 
			@PathVariable("checkin")@DateTimeFormat(pattern="yyyy-MM-dd") Date checkin,
			@PathVariable("checkout")@DateTimeFormat(pattern="yyyy-MM-dd") Date checkout, 
			@PathVariable("adult_qtd") Integer adult,
			@PathVariable("child_qtd") Integer child) {
		try {
			QueryRequest hotelRequest = new QueryRequest(hotelId, checkin, checkout, adult, child);
			return ResponseEntity.status(HttpStatus.OK).body(hotelService.hotelDetails(hotelRequest));
		}catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
}
