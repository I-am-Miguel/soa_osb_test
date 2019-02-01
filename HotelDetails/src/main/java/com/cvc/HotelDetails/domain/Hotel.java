package com.cvc.HotelDetails.domain;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Hotel {

	private String id;
	private String name;
	private String cityCode;
	private String cityName;
	private List<Room> rooms;

}
