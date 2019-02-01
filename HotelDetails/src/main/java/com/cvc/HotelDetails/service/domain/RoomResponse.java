package com.cvc.HotelDetails.service.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomResponse {

	public static BigDecimal COMMISSION = new BigDecimal("0.7");
	
	
	private Integer roomID;
	private String categoryName;
	private BigDecimal totalPrice;
	private PriceResponse priceDetail;
	public void calculatePriceTotal(LocalDate checkin, LocalDate Checkout, Integer adult, Integer child) {
		Long duration = Duration.between(checkin.atStartOfDay(), Checkout.plusDays(1).atStartOfDay()).toDays();
		
		BigDecimal totalPriceAdult = priceDetail.getPricePerDayAdult().multiply(BigDecimal.valueOf(adult)).multiply(BigDecimal.valueOf(duration));
		BigDecimal totalPriceChild = priceDetail.getPricePerDayChild().multiply(BigDecimal.valueOf(child)).multiply(BigDecimal.valueOf(duration));
		
		totalPrice = totalPriceAdult.add(totalPriceChild).divide(COMMISSION, 2, RoundingMode.HALF_EVEN);
	}

}
