package com.hirese.service.repository;

import com.hirese.service.entity.BookingOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface BookingOrderRepository extends JpaRepository<BookingOrder, UUID> {

    @Query(value = "SELECT * \n" +
            "FROM public.booking_orders\n" +
            "where start_ordering_date >= cast (?1 as DATE ) and end_ordering_date <= cast (?2 as DATE)\n" +
            "or start_ordering_date >= cast (?1 as DATE ) and end_ordering_date >= cast (?2 as DATE) and start_ordering_date <=  cast (?2 as DATE)\n" +
            "or start_ordering_date <= cast (?1 as DATE) and end_ordering_date <= cast (?2 as DATE) and end_ordering_date>= cast(?1 as DATE)\n" +
            "or start_ordering_date >= cast (?1 as DATE) and end_ordering_date >= cast (?2 as DATE)",
            nativeQuery = true)
    List<BookingOrder> findAllOrdersIntersectDates(String startDate, String endDate);
}
