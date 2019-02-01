package com.cvc.reservation.service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cvc.reservation.config.ReservationTask;
import com.cvc.reservation.domain.Hotel;
import com.cvc.reservation.repository.HotelRepository;
import com.cvc.reservation.service.domain.HotelResponse;
import com.cvc.reservation.service.domain.QueryRequest;

import reactor.core.publisher.Flux;

@Service
public class FluxHotelService {

	@Autowired
	private HotelRepository hotelRepository;
	@Autowired
	private ReservationTask reservationTask;

	public Flux<HotelResponse> checkReservation(QueryRequest hotelRequest) {
		Flux<Hotel> hotels = hotelRepository.findAllByCityCode(hotelRequest.getCityCode());

		return FluxParser.hotelsToResponse(
				hotels.switchIfEmpty(Flux.fromIterable(reservationTask.getHotelsByCity(hotelRequest.getCityCode()))),
				LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(hotelRequest.getCheckin())), 
				LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(hotelRequest.getCheckout())),
				hotelRequest.getAdult(),hotelRequest.getChild());
	}
	
	public Flux<Hotel> saveAll(List<Hotel> hotels) {
		return hotelRepository.saveAll(hotels);
	}

	public void deleteAll() {
		hotelRepository.deleteAll();
	}

}
