package com.cvc.HotelDetails;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.Temporal;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.cvc.HotelDetails.service.domain.HotelResponse;
import com.cvc.HotelDetails.service.domain.QueryRequest;
import com.cvc.HotelDetails.service.domain.RoomResponse;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class HotelDetailsApplicationTests {

	@Autowired
	private TestRestTemplate restTemplate;

	public static Stream<Arguments> favoriteHotels() throws ParseException {
		return Stream.of(
				Arguments.of(new QueryRequest(1, new SimpleDateFormat("yyyy-MM-dd").parse(LocalDate.now().toString()), 
							new SimpleDateFormat("yyyy-MM-dd").parse(LocalDate.now().toString()), 2, 3)),
				Arguments.of(new QueryRequest(2, new SimpleDateFormat("yyyy-MM-dd").parse(LocalDate.now().toString()), 
							new SimpleDateFormat("yyyy-MM-dd").parse(LocalDate.now().plusDays(30).toString()), 2, 10)),
				Arguments.of(new QueryRequest(100, new SimpleDateFormat("yyyy-MM-dd").parse(LocalDate.now().toString()), 
							new SimpleDateFormat("yyyy-MM-dd").parse(LocalDate.now().plusDays(1).toString()), 2, 0)),
				Arguments.of(new QueryRequest(40, new SimpleDateFormat("yyyy-MM-dd").parse(LocalDate.now().toString()), 
							new SimpleDateFormat("yyyy-MM-dd").parse(LocalDate.now().plusDays(15).toString()), 30, 0)),
				Arguments.of(new QueryRequest(20, new SimpleDateFormat("yyyy-MM-dd").parse(LocalDate.now().toString()), 
							new SimpleDateFormat("yyyy-MM-dd").parse(LocalDate.now().plusDays(30).toString()), 2, 40)));
	}

	@DisplayName("Should Service Available")
	@ParameterizedTest(name = "{index} => hotelTest={0}")
	@MethodSource("favoriteHotels")
	public void serviceAvailability(QueryRequest hotelTest) {
		ResponseEntity<HotelResponse> response = restTemplate.getForEntity(hotelTest.url(), HotelResponse.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertNotNull(response.getBody());
	}

	@DisplayName("Chosen City")
	@ParameterizedTest(name = "{index} => hotelTest={0}")
	@MethodSource("favoriteHotels")
	public void chosenCity(QueryRequest hotelTest) {
		ResponseEntity<HotelResponse> response = restTemplate.getForEntity(hotelTest.url(), HotelResponse.class);
		assertNotNull(response.getBody());
		assertTrue("Not have City", !response.getBody().getRooms().isEmpty());
	}

	@DisplayName("Reserve Price")
	@ParameterizedTest(name = "{index} => hotelTest={0}")
	@MethodSource("favoriteHotels")
	public void reservePrice(QueryRequest hotelTest) {
		ResponseEntity<HotelResponse> response = restTemplate.getForEntity(hotelTest.url(), HotelResponse.class);
		assertNotNull(response.getBody());
		assertTrue("City not have Vacancies: " + hotelTest.getId(), !response.getBody().getRooms().isEmpty());

		Temporal checkin = LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(hotelTest.getCheckin())).atStartOfDay();
		Temporal checkout = LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(hotelTest.getCheckout())).plusDays(1).atStartOfDay();
		
		
		Long duration = Duration.between(checkin,checkout).toDays();

		assertThat(response.getBody().getRooms().stream().allMatch(room -> {

			BigDecimal priceTotalAdult = room.getPriceDetail().getPricePerDayAdult()
					.multiply(new BigDecimal(hotelTest.getAdult())).multiply(new BigDecimal(duration));

			BigDecimal priceTotalChild = room.getPriceDetail().getPricePerDayChild()
					.multiply(new BigDecimal(hotelTest.getChild())).multiply(new BigDecimal(duration));

			return room.getTotalPrice().equals(
					priceTotalAdult.add(priceTotalChild).divide(RoomResponse.COMMISSION, 2, RoundingMode.HALF_EVEN));
		}));
	}

}
