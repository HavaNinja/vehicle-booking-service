package com.hirese.service.unit.service;

import com.hirese.service.dto.ReservationCalculationResponse;
import com.hirese.service.entity.BookingOrder;
import com.hirese.service.entity.Category;
import com.hirese.service.entity.User;
import com.hirese.service.entity.Vehicle;
import com.hirese.service.exception.HireServiceException;
import com.hirese.service.repository.BookingOrderRepository;
import com.hirese.service.repository.VehicleRepository;
import com.hirese.service.service.UserService;
import com.hirese.service.service.VehicleService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class VehicleServiceTest extends BaseServiceTest {
    @InjectMocks
    private VehicleService vehicleService;

    @Mock
    private VehicleRepository vehicleRepository;
    @Mock
    private BookingOrderRepository bookingOrderRepository;
    @Mock
    private UserService userService;

    @After
    public void afterEach() {
        Mockito.verifyNoMoreInteractions(vehicleRepository);
        Mockito.verifyNoMoreInteractions(bookingOrderRepository);
        Mockito.verifyNoMoreInteractions(userService);
    }

    @Test
    public void shouldReturnAllVehiclesFromDatabase() {
        final Vehicle vehicleOne = new Vehicle();
        vehicleOne.setVehicleId(UUID.randomUUID());
        vehicleOne.setRegistrationNumber(UUID.randomUUID().toString());

        final Vehicle vehicleTwo = new Vehicle();
        vehicleTwo.setVehicleId(UUID.randomUUID());
        vehicleTwo.setRegistrationNumber(UUID.randomUUID().toString());

        when(vehicleRepository.findAll()).thenReturn(List.of(vehicleOne, vehicleTwo));

        final List<Vehicle> allVehicles = vehicleService.getAllVehicles();

        assertEquals(2, allVehicles.size());
        verify(vehicleRepository).findAll();
    }

    @Test
    public void shouldReturnAllAvailableVehiclesForSpecificDates() {
        final String startDate = LocalDate.now().toString();
        final String endDate = LocalDate.now().toString();

        final Vehicle vehicleOne = new Vehicle();
        vehicleOne.setVehicleId(UUID.randomUUID());
        vehicleOne.setRegistrationNumber(UUID.randomUUID().toString());

        final Vehicle vehicleTwo = new Vehicle();
        vehicleTwo.setVehicleId(UUID.randomUUID());
        vehicleTwo.setRegistrationNumber(UUID.randomUUID().toString());

        final BookingOrder bookingOrder = new BookingOrder();
        bookingOrder.setVehicles(List.of(vehicleOne));
        final List<Vehicle> vehicles = new ArrayList<>();
        vehicles.add(vehicleOne);
        vehicles.add(vehicleTwo);

        when(vehicleRepository.findAll()).thenReturn(vehicles);
        when(bookingOrderRepository.findAllOrdersIntersectDates(eq(startDate), eq(endDate))).thenReturn(List.of(bookingOrder));

        final List<Vehicle> allAvailableVehicles = vehicleService.getAllAvailableVehiclesByDate(startDate, endDate);

        assertEquals(1, allAvailableVehicles.size());
        assertEquals(vehicleTwo, allAvailableVehicles.get(0));

        verify(vehicleRepository).findAll();
        verify(bookingOrderRepository).findAllOrdersIntersectDates(eq(startDate), eq(endDate));
    }

    @Test
    public void shouldReturn25WhenOrderContainsOneCarWhichConst25() {
        final Category category = new Category();
        category.setPricePerDay(BigDecimal.valueOf(25L));

        final String startDate = LocalDate.now().toString();
        final String endDate = LocalDate.now().toString();

        final Vehicle vehicleOne = new Vehicle();
        vehicleOne.setVehicleId(UUID.randomUUID());
        vehicleOne.setRegistrationNumber("ZXC 123");
        vehicleOne.setCategoryOfVehicle(category);
        vehicleOne.setMake("Scoda");

        when(vehicleRepository.findByRegistrationNumberIn(eq(List.of("ZXC 123")))).thenReturn(List.of(vehicleOne));

        final ReservationCalculationResponse reservationCalculationResponse = vehicleService.calculateCostForOrder(List.of("ZXC 123"), startDate, endDate);

        Assert.assertEquals(25L, reservationCalculationResponse.getPrice().longValue());
        Assert.assertEquals(endDate, reservationCalculationResponse.getEndDate());
        Assert.assertEquals(startDate, reservationCalculationResponse.getStarDate());

        verify(vehicleRepository).findByRegistrationNumberIn(eq(List.of("ZXC 123")));
    }

    @Test
    public void shouldReserveAvailableVehicles() {
        final String startDate = LocalDate.now().toString();
        final String endDate = LocalDate.now().toString();

        final Category category = new Category();
        category.setPricePerDay(BigDecimal.valueOf(25L));

        final User user = new User();
        final UUID userId = UUID.randomUUID();
        user.setUserId(userId);

        final Vehicle vehicleOne = new Vehicle();
        vehicleOne.setVehicleId(UUID.randomUUID());
        vehicleOne.setRegistrationNumber(UUID.randomUUID().toString());
        vehicleOne.setRegistrationNumber("ZXC 123");
        vehicleOne.setCategoryOfVehicle(category);

        final Vehicle vehicleTwo = new Vehicle();
        vehicleTwo.setVehicleId(UUID.randomUUID());
        vehicleTwo.setRegistrationNumber(UUID.randomUUID().toString());
        vehicleTwo.setRegistrationNumber("QWE 322");
        vehicleTwo.setCategoryOfVehicle(category);

        final BookingOrder bookingOrder = new BookingOrder();
        bookingOrder.setVehicles(Collections.emptyList());
        final List<Vehicle> vehicles = new ArrayList<>();
        vehicles.add(vehicleOne);
        vehicles.add(vehicleTwo);

        when(vehicleRepository.findByRegistrationNumberIn(List.of("ZXC 123", "QWE 322"))).thenReturn(vehicles);
        when(bookingOrderRepository.findAllOrdersIntersectDates(eq(startDate), eq(endDate))).thenReturn(List.of(bookingOrder));
        when(userService.getUserById(eq(userId))).thenReturn(Optional.of(user));

        vehicleService.reserve(List.of("ZXC 123", "QWE 322"), startDate, endDate, userId);

        verify(vehicleRepository).findByRegistrationNumberIn(eq(List.of("ZXC 123", "QWE 322")));
        verify(bookingOrderRepository).findAllOrdersIntersectDates(eq(startDate), eq(endDate));
        verify(bookingOrderRepository).save(any());
        verify(userService).getUserById(eq(userId));
    }

    @Test
    public void shouldReturnCost100WhenOrderContains2CarsAndPricePerDayIs25() {
        final Category category = new Category();
        category.setPricePerDay(BigDecimal.valueOf(25L));

        final Vehicle vehicleOne = new Vehicle();
        vehicleOne.setVehicleId(UUID.randomUUID());
        vehicleOne.setRegistrationNumber(UUID.randomUUID().toString());
        vehicleOne.setCategoryOfVehicle(category);

        final Vehicle vehicleTwo = new Vehicle();
        vehicleTwo.setVehicleId(UUID.randomUUID());
        vehicleTwo.setRegistrationNumber(UUID.randomUUID().toString());
        vehicleTwo.setCategoryOfVehicle(category);

        final String startDate = "2015-02-16";
        final String endDate = "2015-02-17";

        final BigDecimal result = ReflectionTestUtils.invokeMethod(vehicleService, "getTotalRentalCost", startDate, endDate, List.of(vehicleOne, vehicleTwo));
        assertEquals(100L, result.longValue());

    }

    @Test
    public void shouldReturn50WhenConstPerDayIs25AndQuantityOfDays2() {
        final BigDecimal costPerDay = BigDecimal.valueOf(25L);
        final Long days = 2L;

        final BigDecimal result = ReflectionTestUtils.invokeMethod(vehicleService, "calculateReservationCost", costPerDay, days);

        assertEquals(50L, result.longValue());
    }

    @Test
    public void shouldThrowExceptionWhenDateIsInPast() {
        final String startDate = "2015-02-16";
        final String endDate = "2015-02-16";

        Assert.assertThrows(HireServiceException.class, () -> ReflectionTestUtils.invokeMethod(vehicleService, "validateDates", startDate, endDate));
    }

    @Test
    public void shouldThrowExceptionWhenStartDateIsBeforeAndDate() {
        final String startDate = LocalDate.now().plusDays(10).toString();
        final String endDate = LocalDate.now().plusDays(1).toString();

        Assert.assertThrows(HireServiceException.class, () -> ReflectionTestUtils.invokeMethod(vehicleService, "validateDates", startDate, endDate));
    }
}
