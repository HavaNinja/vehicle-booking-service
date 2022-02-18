package com.hirese.service.service;

import com.hirese.service.dto.ReservationCalculationResponse;
import com.hirese.service.entity.BookingOrder;
import com.hirese.service.entity.User;
import com.hirese.service.entity.Vehicle;
import com.hirese.service.exception.HireServiceException;
import com.hirese.service.repository.BookingOrderRepository;
import com.hirese.service.repository.VehicleRepository;
import com.hirese.service.util.HireServiceUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.hirese.service.util.HireServiceUtils.daysBetweenRangeIncluded;
import static com.hirese.service.util.HireServiceUtils.isDateInPast;

@Service
@RequiredArgsConstructor
public class VehicleService {
    private final VehicleRepository vehicleRepository;
    private final BookingOrderRepository bookingOrderRepository;
    private final UserService userService;

    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }

    public List<Vehicle> getAllAvailableVehiclesByDate(final String requiredStartDate, final String requiredEndDate) {
        validateDates(requiredStartDate, requiredEndDate);

        final List<Vehicle> allVehicles = vehicleRepository.findAll();
        final List<BookingOrder> bookingOrdersForDates = bookingOrderRepository.findAllOrdersIntersectDates(requiredStartDate, requiredEndDate);
        final List<Vehicle> notAvailableVehicles = bookingOrdersForDates.stream()
                .flatMap(bookingOrder -> bookingOrder.getVehicles().stream())
                .collect(Collectors.toList());
        allVehicles.removeAll(notAvailableVehicles);
        return allVehicles;
    }

    public ReservationCalculationResponse calculateCostForOrder(final List<String> registrationNumbers, final String startDate, final String endDate) {
        validateDates(startDate, endDate);
        final List<Vehicle> requiredVehicle = vehicleRepository.findByRegistrationNumberIn(registrationNumbers);
        if (requiredVehicle.isEmpty()) {
            throw new HireServiceException("Could not find any vehicle by registration numbers", HttpStatus.NOT_FOUND.value());
        }
        final BigDecimal totalPrice = getTotalRentalCost(startDate, endDate, requiredVehicle);

        final ReservationCalculationResponse reservationCalculationResponse = new ReservationCalculationResponse();
        reservationCalculationResponse.setCars(requiredVehicle.stream().collect(Collectors.toMap(Vehicle::getRegistrationNumber, Vehicle::getMake)));
        reservationCalculationResponse.setPrice(totalPrice);
        reservationCalculationResponse.setStarDate(startDate);
        reservationCalculationResponse.setEndDate(endDate);

        return reservationCalculationResponse;
    }

    public void reserve(final List<String> registrationNumber, final String startDate, final String endDate, final UUID userId) {
        validateDates(startDate, endDate);
        final Optional<User> user = userService.getUserById(userId);
        user.orElseThrow(() -> new HireServiceException(String.format("User with id [%s] not found", userId.toString()), HttpStatus.NOT_FOUND.value()));
        final List<Vehicle> requiredVehicles = vehicleRepository.findByRegistrationNumberIn(registrationNumber);
        if (requiredVehicles.isEmpty()) {
            throw new HireServiceException("Could not find any vehicle by registration numbers", HttpStatus.NOT_FOUND.value());
        }
        if (checkAvailableVehicles(registrationNumber, startDate, endDate)) {
            final BigDecimal totalPrice = getTotalRentalCost(startDate, endDate, requiredVehicles);
            final BookingOrder bookingOrder = new BookingOrder();
            bookingOrder.setOrderId(UUID.randomUUID());
            bookingOrder.setPrice(totalPrice);
            bookingOrder.setUser(user.get());
            bookingOrder.setStartOrderingDate(LocalDate.parse(startDate, HireServiceUtils.FORMATTER));
            bookingOrder.setEndOrderingDate(LocalDate.parse(endDate, HireServiceUtils.FORMATTER));
            bookingOrder.setVehicles(requiredVehicles);
            bookingOrderRepository.save(bookingOrder);
        } else
            throw new HireServiceException(String.format("Vehicle with registration number [%s] in not available for booking in range [%s - %s]", registrationNumber, startDate, endDate),
                    HttpStatus.BAD_REQUEST.value());
    }

    private BigDecimal getTotalRentalCost(final String startDate, final String endDate, final List<Vehicle> requiredVehicle) {
        final long quantityOfRequiredDays = daysBetweenRangeIncluded(startDate, endDate);
        return requiredVehicle.stream()
                .map(x -> x.getCategoryOfVehicle().getPricePerDay())
                .map(price -> calculateReservationCost(price, quantityOfRequiredDays))
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.valueOf(0));
    }

    private BigDecimal calculateReservationCost(final BigDecimal pricePerDay, final long days) {
        return pricePerDay.multiply(BigDecimal.valueOf(days));
    }

    private boolean checkAvailableVehicles(final List<String> registrationNumber, final String startDate, final String endDate) {
        return bookingOrderRepository.findAllOrdersIntersectDates(startDate, endDate).stream()
                .flatMap(bookingOrder -> bookingOrder.getVehicles().stream())
                .map(Vehicle::getRegistrationNumber)
                .filter(registrationNumber::contains)
                .findAny().isEmpty();
    }


    private void validateDates(final String requiredStartDate, final String requiredEndDate) {
        if (isDateInPast(requiredStartDate) || isDateInPast(requiredEndDate)) {
            throw new HireServiceException("Date can not be in past", HttpStatus.BAD_REQUEST.value());
        } else if (LocalDate.parse(requiredStartDate).isAfter(LocalDate.parse(requiredEndDate))) {
            throw new HireServiceException("End date can not be before start date", HttpStatus.BAD_REQUEST.value());
        }
    }
}
