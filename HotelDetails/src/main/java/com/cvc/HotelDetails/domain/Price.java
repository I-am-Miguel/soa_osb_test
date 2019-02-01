package com.cvc.HotelDetails.domain;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Price{

	private BigDecimal adult;	
	private BigDecimal child;

}
