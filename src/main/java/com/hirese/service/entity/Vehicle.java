package com.hirese.service.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hirese.service.entity.enums.Fuel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "VEHICLES")
public class Vehicle {
    @Id
    private UUID vehicleId;
    private String registrationNumber;
    private String make;
    private String model;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category categoryOfVehicle;
    @Enumerated(EnumType.STRING)
    private Fuel fuel;
    @ManyToMany(mappedBy = "vehicles")
    @JsonIgnore
    private List<BookingOrder> orders;

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vehicle vehicle = (Vehicle) o;
        return Objects.equals(vehicleId, vehicle.vehicleId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vehicleId);
    }
}
