package com.cvc.reservation.resource;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cvc.reservation.service.FluxHotelService;
import com.cvc.reservation.service.domain.HotelResponse;
import com.cvc.reservation.service.domain.QueryRequest;

import io.swagger.annotations.Api;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping(value = "/rest", produces = "application/json")
@Api(tags = "Reservation Service")
public class ReservationController {

	@Autowired
	private FluxHotelService fluxHotelService;

	@RequestMapping(method = RequestMethod.GET, value = "/reservation/{cityCode}/{checkin}/{checkout}/{adult_qtd}/{child_qtd}/")
	public Flux<HotelResponse> checkReservation(@PathVariable("cityCode") Integer cityCode,
			@PathVariable("checkin") @DateTimeFormat(pattern = "yyyy-MM-dd") Date checkin,
			@PathVariable("checkout") @DateTimeFormat(pattern = "yyyy-MM-dd") Date checkout,
			@PathVariable("adult_qtd") Integer adult, @PathVariable("child_qtd") Integer child) {
		try {
			QueryRequest hotelRequest = new QueryRequest(cityCode, checkin, checkout, adult, child);
			return fluxHotelService.checkReservation(hotelRequest);
		} catch (Exception e) {
			return Flux.empty();
		}
	}
}
