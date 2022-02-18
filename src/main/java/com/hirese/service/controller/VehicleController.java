package com.hirese.service.controller;

import com.hirese.service.dto.GenericJsonResponse;
import com.hirese.service.dto.GenericJsonResponseList;
import com.hirese.service.dto.ReservationCalculationRequest;
import com.hirese.service.dto.ReservationCalculationResponse;
import com.hirese.service.dto.ReservationRequest;
import com.hirese.service.entity.Vehicle;
import com.hirese.service.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.hirese.service.util.HireServiceUtils.wrapResponse;
import static com.hirese.service.util.HireServiceUtils.wrapResponseList;

@RestController
@RequestMapping("/api/vehicle")
@RequiredArgsConstructor
public class VehicleController {
    private final VehicleService vehicleService;

    @GetMapping("/all")
    public GenericJsonResponseList<Vehicle> getAllVehicles() {
        return wrapResponseList(vehicleService.getAllVehicles());
    }

    @GetMapping("/available")
    public GenericJsonResponseList<Vehicle> getAvailableVehiclesByDate(@RequestParam final String startDate, @RequestParam String endDate) {
        return wrapResponseList(vehicleService.getAllAvailableVehiclesByDate(startDate, endDate));
    }

    @PostMapping("/cost")
    public GenericJsonResponse<ReservationCalculationResponse> getRentalCost(@RequestBody @Valid final ReservationCalculationRequest request) {
        return wrapResponse(vehicleService.calculateCostForOrder(request.getRegistrationNumbers(), request.getStartDate(), request.getEndDate()));
    }

    @PostMapping("/reservation")
    public ResponseEntity<Void> reserveVehicles(@RequestBody @Valid final ReservationRequest request) {
        vehicleService.reserve(request.getRegistrationNumbers(), request.getStartDate(), request.getEndDate(), request.getUserId());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
