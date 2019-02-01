package com.cvc.HotelDetails;

import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;

import com.cvc.reservation.ReservationApplication;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@ComponentScan("com.cvc")
@EnableSwagger2
@SpringBootApplication
public class HotelDetailsApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder()
			.bannerMode(Banner.Mode.CONSOLE)
			.sources(ReservationApplication.class, HotelDetailsApplication.class)
			.run(args);
	}

}
