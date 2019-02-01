package com.cvc.HotelDetails.service.domain;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HotelResponse {
	
	private Integer id;
	private String cityName;
	private List<RoomResponse> rooms;

}
