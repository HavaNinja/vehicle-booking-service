package com.hirese.service.repository;

import com.hirese.service.entity.Vehicle;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface VehicleRepository extends JpaRepository<Vehicle, UUID> {
    @EntityGraph(
            type = EntityGraph.EntityGraphType.FETCH,
            attributePaths = {
                    "categoryOfVehicle",
                    "orders",
                    "orders.user"
            }
    )
    List<Vehicle> findAll();

    List<Vehicle> findByRegistrationNumberIn(List<String> registrationNumbers);
}
