package com.cvc.reservation.domain;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class Hotel {

	@Id
	private String id;
	private String name;
	private Integer cityCode;
	private String cityName;
	private List<Room> rooms;

}
