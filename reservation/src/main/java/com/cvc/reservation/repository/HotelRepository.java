package com.cvc.reservation.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.cvc.reservation.domain.Hotel;

import reactor.core.publisher.Flux;

public interface HotelRepository extends ReactiveMongoRepository<Hotel, String> {

	Flux<Hotel> findAllByCityCode(Integer cityCode);
	
}
