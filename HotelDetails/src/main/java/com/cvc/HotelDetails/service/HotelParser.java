package com.cvc.HotelDetails.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.cvc.HotelDetails.domain.Hotel;
import com.cvc.HotelDetails.domain.Price;
import com.cvc.HotelDetails.domain.Room;
import com.cvc.HotelDetails.service.domain.HotelResponse;
import com.cvc.HotelDetails.service.domain.PriceResponse;
import com.cvc.HotelDetails.service.domain.RoomResponse;

@Component
public class HotelParser {

	public static HotelResponse hotelToResponse(Hotel hotel, LocalDate checkin, LocalDate checkout, Integer adult, Integer child) {
		HotelResponse hotelResponse = new HotelResponse(Integer.valueOf(hotel.getId()),hotel.getCityName(), new ArrayList<>());
		hotelResponse.setRooms(roomsToResponse(hotel.getRooms(), checkin, checkout,adult,child));
		
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
