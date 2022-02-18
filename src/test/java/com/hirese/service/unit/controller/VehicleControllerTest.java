package com.hirese.service.unit.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hirese.service.controller.VehicleController;
import com.hirese.service.dto.GenericJsonResponse;
import com.hirese.service.dto.GenericJsonResponseList;
import com.hirese.service.dto.ReservationCalculationResponse;
import com.hirese.service.entity.Vehicle;
import com.hirese.service.service.VehicleService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(VehicleController.class)
public class VehicleControllerTest extends BaseControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VehicleService vehicleService;

    @Autowired
    private ObjectMapper objectMapper;

    @After
    public void afterEach() {
        Mockito.verifyNoMoreInteractions(vehicleService);
    }

    @Test
    public void shouldReturnAllVehicles() throws Exception {
        final Vehicle vehicleOne = new Vehicle();
        vehicleOne.setVehicleId(UUID.randomUUID());
        vehicleOne.setRegistrationNumber(UUID.randomUUID().toString());

        when(vehicleService.getAllVehicles()).thenReturn(List.of(vehicleOne));

        final String jsonResponse = mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/vehicle/all"))
                .andExpect(status()
                        .isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        final GenericJsonResponseList<Vehicle> responseBody = objectMapper.readValue(jsonResponse, new TypeReference<>() {
        });

        final Vehicle vehicleFromResponse = responseBody.getData().get(0);
        Assert.assertEquals(vehicleOne.getVehicleId(), vehicleFromResponse.getVehicleId());
        Assert.assertEquals(vehicleOne.getRegistrationNumber(), vehicleFromResponse.getRegistrationNumber());

        verify(vehicleService).getAllVehicles();
    }

    @Test
    public void shouldReturnAvailableVehiclesForTheGivenDay() throws Exception {
        final Vehicle vehicleOne = new Vehicle();
        vehicleOne.setVehicleId(UUID.randomUUID());
        vehicleOne.setRegistrationNumber(UUID.randomUUID().toString());

        when(vehicleService.getAllAvailableVehiclesByDate("2022-02-17", "2022-02-17")).thenReturn(List.of(vehicleOne));

        final String jsonResponse = mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/vehicle/available?startDate=2022-02-17&endDate=2022-02-17")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        final GenericJsonResponseList<Vehicle> responseBody = objectMapper.readValue(jsonResponse, new TypeReference<>() {
        });

        final Vehicle vehicleFromResponse = responseBody.getData().get(0);
        Assert.assertEquals(vehicleOne.getVehicleId(), vehicleFromResponse.getVehicleId());
        Assert.assertEquals(vehicleOne.getRegistrationNumber(), vehicleFromResponse.getRegistrationNumber());

        verify(vehicleService).getAllAvailableVehiclesByDate("2022-02-17", "2022-02-17");
    }

    @Test
    public void shouldReturnRentalCostForTheSpecificVehicle() throws Exception {
        final ReservationCalculationResponse mockResponse = new ReservationCalculationResponse();
        mockResponse.setStarDate("2022-02-21");
        mockResponse.setEndDate("2022-02-21");
        when(vehicleService.calculateCostForOrder(List.of("ZED 852"), "2022-02-21", "2022-02-21")).thenReturn(mockResponse);
        final String jsonResponse = mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/vehicle/cost")
                        .content("{\n" +
                                "    \"startDate\":\"2022-02-21\",\n" +
                                "    \"endDate\":\"2022-02-21\",\n" +
                                "    \"registrationNumbers\":[\"ZED 852\"]\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        final GenericJsonResponse<ReservationCalculationResponse> responseBody = objectMapper.readValue(jsonResponse, new TypeReference<>() {
        });
        Assert.assertEquals(mockResponse.getEndDate(), responseBody.getData().getEndDate());
        Assert.assertEquals(mockResponse.getStarDate(), responseBody.getData().getStarDate());
        verify(vehicleService).calculateCostForOrder(List.of("ZED 852"), "2022-02-21", "2022-02-21");
    }

    @Test
    public void shouldReserveTheVehicle() throws Exception {
        mockMvc.perform(post("/api/vehicle/reservation").content("{\n" +
                "    \"startDate\":\"2022-02-21\",\n" +
                "    \"endDate\":\"2022-02-21\",\n" +
                "    \"registrationNumbers\":[\"ZED 852\"],\n" +
                "    \"userId\":\"c7c84323-5623-475c-abd4-c81dba79860f\"\n" +
                "}").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());

        verify(vehicleService).reserve(List.of("ZED 852"), "2022-02-21", "2022-02-21", UUID.fromString("c7c84323-5623-475c-abd4-c81dba79860f"));
    }

}
