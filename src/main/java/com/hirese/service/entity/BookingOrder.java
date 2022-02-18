package com.hirese.service.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "BOOKING_ORDERS")
public class BookingOrder {
    @Id
    private UUID orderId;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "booking_order_vehicle",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "vehicle_id"))
    private List<Vehicle> vehicles;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    private BigDecimal price;
    private LocalDate startOrderingDate;
    private LocalDate endOrderingDate;
}
