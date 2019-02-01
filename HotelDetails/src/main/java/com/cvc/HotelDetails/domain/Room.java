package com.cvc.HotelDetails.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Room{

	private String roomID;
	private String categoryName;
	private Price price;


}
