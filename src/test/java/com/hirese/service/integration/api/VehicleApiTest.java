package com.hirese.service.integration.api;

import com.hirese.service.dto.GenericJsonResponseList;
import com.hirese.service.dto.ReservationRequest;
import com.hirese.service.entity.Vehicle;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;


public class VehicleApiTest extends BaseApiIntegrationTest {
    private final UUID testUserId = UUID.fromString("515abc5d-88f3-4ea0-9c2a-aa1b78c4eaf6");

    @Test
    public void shouldReturnAllAvailableVehicles() {
        final String allVehicleUrl = String.format("http://localhost:%d/api/vehicle/all", port);
        final ResponseEntity<GenericJsonResponseList<Vehicle>> response = restTemplate.exchange(allVehicleUrl, HttpMethod.GET, null, new ParameterizedTypeReference<>() {
        });

        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assert.assertFalse(response.getBody().getData().isEmpty());
    }

    @Test
    public void shouldReturnAllVehiclesWhichAreAvailableForToday() {
        final String availableVehicleUrl = String.format("http://localhost:%d/api/vehicle/available?startDate=%s&endDate=%s", port, LocalDate.now(), LocalDate.now());
        final ResponseEntity<GenericJsonResponseList<Vehicle>> response = restTemplate.exchange(availableVehicleUrl, HttpMethod.GET, null, new ParameterizedTypeReference<>() {
        });

        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assert.assertFalse(response.getBody().getData().isEmpty());
    }

    @Test
    public void shouldReserveTheVehicle() {
        final String availableVehicleUrl = String.format("http://localhost:%d/api/vehicle/available?startDate=%s&endDate=%s", port, LocalDate.now(), LocalDate.now());
        final String reservationVehicleUrl = String.format("http://localhost:%d/api/vehicle/reservation", port);
        final ResponseEntity<GenericJsonResponseList<Vehicle>> availableVehiclesResponseBody = restTemplate.exchange(availableVehicleUrl, HttpMethod.GET, null, new ParameterizedTypeReference<>() {
        });

        final Vehicle vehicle = availableVehiclesResponseBody.getBody().getData().get(0);

        final ReservationRequest reservationRequest = new ReservationRequest();
        reservationRequest.setStartDate(LocalDate.now().toString());
        reservationRequest.setEndDate(LocalDate.now().toString());
        reservationRequest.setUserId(testUserId);
        reservationRequest.setRegistrationNumbers(List.of(vehicle.getRegistrationNumber()));

        final ResponseEntity<Void> reservationResponse = restTemplate.exchange(RequestEntity.post(reservationVehicleUrl).body(reservationRequest), new ParameterizedTypeReference<>() {
        });

        Assert.assertEquals(HttpStatus.CREATED, reservationResponse.getStatusCode());

        final ResponseEntity<GenericJsonResponseList<Vehicle>> availableVehiclesAfterReservation = restTemplate.exchange(availableVehicleUrl, HttpMethod.GET, null, new ParameterizedTypeReference<>() {
        });

        Assert.assertTrue(availableVehiclesResponseBody.getBody().getData().size() > availableVehiclesAfterReservation.getBody().getData().size());
    }
}
