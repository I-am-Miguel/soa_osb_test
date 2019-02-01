package com.cvc.reservation.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.cvc.reservation.domain.Hotel;
import com.cvc.reservation.service.FluxHotelService;
import com.google.common.collect.Lists;

@Component
public class ReservationTask{

	private static final Logger LOG = LoggerFactory.getLogger(ReservationTask.class);
	private static final List<String> citys = Lists.newArrayList("1032", "7110", "9626");

	@Autowired private RestTemplate restTemplate;
	@Autowired private FluxHotelService fluxHotelService;

	@Value("${urlAvail}")
	private String urlAvail;
	private List<Hotel> hotels = new ArrayList<>();

	@Scheduled(fixedRate = 300000)
	public void run() {
		this.hotels.clear();
		fluxHotelService.deleteAll();
		citys.parallelStream().forEach(city -> {
			this.hotels.addAll(getResultTheCitys(city));
		});
		LOG.debug("Saving results of the all cities");
		fluxHotelService.saveAll(hotels);
	}

	public List<Hotel> getResultTheCitys(String city) {
		LOG.debug("Accessing results of the city  [" + city + "]");
		ResponseEntity<Hotel[]> response = restTemplate.getForEntity(urlAvail + city, Hotel[].class);
		return Arrays.stream(response.getBody()).collect(Collectors.toList());
	}
	
	public List<Hotel> getHotels(){
		return this.hotels;
	}
	
	public List<Hotel> getHotelsByCity(Integer city){
		return hotels.stream()
				.filter(hotel -> hotel.getCityCode().equals(city))
				.collect(Collectors.toList());
	}

}
