package com.hirese.service.unit.repository;

import com.hirese.service.entity.Vehicle;
import com.hirese.service.repository.VehicleRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;


public class VehicleRepositoryTest extends BaseJpaTest {
    @Autowired
    private VehicleRepository vehicleRepository;

    @Before
    public void before() {
        final Vehicle vehicle = new Vehicle();
        vehicle.setVehicleId(UUID.randomUUID());
        vehicle.setRegistrationNumber("AZX 123");
        vehicleRepository.save(vehicle);
    }

    @Test
    public void shouldReturnAllVehicles() {
        final List<Vehicle> all = vehicleRepository.findAll();
        Assert.assertEquals(1, all.size());
        assertFalse(all.isEmpty());
    }

    @Test
    public void shouldReturnVehicleByRegistrationNumber() {
        final List<Vehicle> all = vehicleRepository.findByRegistrationNumberIn(List.of("AZX 123"));
        final Vehicle vehicle = all.get(0);
        assertEquals("AZX 123", vehicle.getRegistrationNumber());
    }
}