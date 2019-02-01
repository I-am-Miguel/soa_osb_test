package com.cvc.reservation.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.cvc.reservation.domain.Hotel;
import com.cvc.reservation.domain.Price;
import com.cvc.reservation.domain.Room;
import com.cvc.reservation.service.domain.HotelResponse;
import com.cvc.reservation.service.domain.PriceResponse;
import com.cvc.reservation.service.domain.RoomResponse;

import reactor.core.publisher.Flux;

@Component
public class FluxParser {

	public static Flux<HotelResponse> hotelsToResponse(Flux<Hotel> hotels, LocalDate checkin, LocalDate checkout, Integer adult, Integer child) {
		Flux<HotelResponse> hotelResponse = hotels.map(h ->{
			return new HotelResponse(Integer.valueOf(h.getId()), h.getCityName(),
					roomsToResponse(h.getRooms(), checkin, checkout,adult,child));
		});
		return hotelResponse;
	}

	private static List<RoomResponse> roomsToResponse(List<Room> rooms, LocalDate checkin, LocalDate checkout, Integer adult, Integer child) {
		List<RoomResponse> rommsResponse = new ArrayList<>();
		rooms.forEach(room -> {
			RoomResponse rommResponse = new RoomResponse(Integer.valueOf(room.getRoomID()), room.getCategoryName(),
					BigDecimal.ZERO, priceToResponse(room.getPrice()));
			rommResponse.calculatePriceTotal(checkin, checkout,adult,child);
			rommsResponse.add(rommResponse);
		});
		return rommsResponse;
	}
	
	private static PriceResponse priceToResponse(Price price) {
		return new PriceResponse(price.getChild(), price.getAdult());
	}

}
