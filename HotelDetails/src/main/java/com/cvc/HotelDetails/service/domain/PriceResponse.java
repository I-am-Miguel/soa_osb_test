package com.cvc.HotelDetails.service.domain;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PriceResponse {

	public BigDecimal pricePerDayChild;
	private BigDecimal pricePerDayAdult;
}
