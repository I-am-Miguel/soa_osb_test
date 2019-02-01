package com.cvc.reservation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.Temporal;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.cvc.reservation.service.domain.HotelResponse;
import com.cvc.reservation.service.domain.QueryRequest;
import com.cvc.reservation.service.domain.RoomResponse;

@Tag("fast")
@DisplayName("Api Integrity Testing")
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class ReservationApplicationTests {

	@Autowired
	private TestRestTemplate restTemplate;

	public static Stream<Arguments> favoriteHotels() throws ParseException {
		return Stream.of(
				Arguments.of(new QueryRequest(1032,new SimpleDateFormat("yyyy-MM-dd").parse(LocalDate.now().toString()),
						new SimpleDateFormat("yyyy-MM-dd").parse(LocalDate.now().toString()),2,3)),
				Arguments.of(new QueryRequest(1032,new SimpleDateFormat("yyyy-MM-dd").parse(LocalDate.now().toString()),
						new SimpleDateFormat("yyyy-MM-dd").parse(LocalDate.now().plusDays(30).toString()),2,3)),
				Arguments.of(new QueryRequest(1032,new SimpleDateFormat("yyyy-MM-dd").parse(LocalDate.now().toString()),
						new SimpleDateFormat("yyyy-MM-dd").parse(LocalDate.now().plusDays(1).toString()),2,0)),
				Arguments.of(new QueryRequest(1032,new SimpleDateFormat("yyyy-MM-dd").parse(LocalDate.now().toString()),
						new SimpleDateFormat("yyyy-MM-dd").parse(LocalDate.now().plusDays(15).toString()),2,0)),
				Arguments.of(new QueryRequest(1032,new SimpleDateFormat("yyyy-MM-dd").parse(LocalDate.now().toString()),
						new SimpleDateFormat("yyyy-MM-dd").parse(LocalDate.now().plusDays(30).toString()),2,0))
			);
	}

	@DisplayName("Should Service Available")
	@ParameterizedTest(name = "{index} => hotelTest={0}")
	@MethodSource("favoriteHotels")
	public void serviceAvailability(QueryRequest hotelTest) {
		ResponseEntity<HotelResponse[]> response = restTemplate.getForEntity(hotelTest.url(), HotelResponse[].class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertNotNull(response.getBody());
	}
	
	@DisplayName("Chosen City")
	@ParameterizedTest(name = "{index} => hotelTest={0}")
	@MethodSource("favoriteHotels")
	public void chosenCity(QueryRequest hotelTest) {
		ResponseEntity<HotelResponse[]> response = restTemplate.getForEntity(hotelTest.url(), HotelResponse[].class);
		assertNotNull(response.getBody());
		assertTrue("Not have City",response.getBody().length > 0);
	}
	
	@DisplayName("We Have Vacancies")
	@ParameterizedTest(name = "{index} => hotelTest={0}")
	@MethodSource("favoriteHotels")
	public void haveVacancies(QueryRequest hotelTest) {
		ResponseEntity<HotelResponse[]> response = restTemplate.getForEntity(hotelTest.url(), HotelResponse[].class);
		assertNotNull(response.getBody());
		assertTrue("City not have Vacancies: "+hotelTest.getCityCode(),response.getBody().length > 0);
		assertThat(response.getBody()).allMatch(h -> !h.getRooms().isEmpty());
	}
	
	@DisplayName("Reserve Price")
	@ParameterizedTest(name = "{index} => hotelTest={0}")
	@MethodSource("favoriteHotels")
	public void reservePrice(QueryRequest hotelTest) {
		ResponseEntity<HotelResponse[]> response = restTemplate.getForEntity(hotelTest.url(), HotelResponse[].class);
		assertNotNull(response.getBody());
		assertTrue("City not have Vacancies: "+hotelTest.getCityCode(),response.getBody().length > 0);
		assertThat(response.getBody()).allMatch(h -> !h.getRooms().isEmpty());
		
		Temporal checkin = LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(hotelTest.getCheckin())).atStartOfDay();
		Temporal checkout = LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(hotelTest.getCheckout())).plusDays(1).atStartOfDay();

		Long duration = Duration.between(checkin,checkout).toDays();
		
		assertThat(response.getBody()).allMatch(h -> h.getRooms().stream().allMatch(room ->{
			
			BigDecimal priceTotalAdult = room.getPriceDetail().getPricePerDayAdult()
					.multiply(new BigDecimal(hotelTest.getAdult()))
					.multiply(new BigDecimal(duration));
			
			BigDecimal priceTotalChild = room.getPriceDetail().getPricePerDayChild()
					.multiply(new BigDecimal(hotelTest.getChild()))
					.multiply(new BigDecimal(duration));
			
			return room.getTotalPrice().equals(priceTotalAdult.add(priceTotalChild).divide(RoomResponse.COMMISSION, 2, RoundingMode.HALF_EVEN));
		}));
	}


}
