package com.cvc.reservation.service.domain;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class HotelResponse {
	
	private Integer id;
	private String cityName;
	private List<RoomResponse> rooms;

}
