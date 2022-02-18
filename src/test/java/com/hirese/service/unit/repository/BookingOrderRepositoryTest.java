package com.hirese.service.unit.repository;

import com.hirese.service.entity.BookingOrder;
import com.hirese.service.repository.BookingOrderRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;


public class BookingOrderRepositoryTest extends BaseJpaTest {
    @Autowired
    private BookingOrderRepository bookingOrderRepository;


    @Before
    public void before() {
        final BookingOrder bookingOrderIn = new BookingOrder();
        bookingOrderIn.setOrderId(UUID.randomUUID());
        bookingOrderIn.setStartOrderingDate(LocalDate.parse("2022-02-22"));
        bookingOrderIn.setEndOrderingDate(LocalDate.parse("2022-02-22"));
        bookingOrderRepository.save(bookingOrderIn);

        final BookingOrder bookingOrderOut = new BookingOrder();
        bookingOrderOut.setOrderId(UUID.randomUUID());
        bookingOrderOut.setStartOrderingDate(LocalDate.parse("2019-02-21"));
        bookingOrderOut.setEndOrderingDate(LocalDate.parse("2022-02-21"));
        bookingOrderRepository.save(bookingOrderOut);

        final BookingOrder bookingOrderBefore = new BookingOrder();
        bookingOrderBefore.setOrderId(UUID.randomUUID());
        bookingOrderBefore.setStartOrderingDate(LocalDate.parse("2019-02-21"));
        bookingOrderBefore.setEndOrderingDate(LocalDate.parse("2022-02-21"));
        bookingOrderRepository.save(bookingOrderBefore);

        final BookingOrder bookingOrderAfter = new BookingOrder();
        bookingOrderAfter.setOrderId(UUID.randomUUID());
        bookingOrderAfter.setStartOrderingDate(LocalDate.parse("2022-02-23"));
        bookingOrderAfter.setEndOrderingDate(LocalDate.parse("2022-02-25"));
        bookingOrderRepository.save(bookingOrderAfter);
    }

    @Test
    public void shouldReturnAllOrdersInAppropriateDates() {
        final List<BookingOrder> all = bookingOrderRepository.findAllOrdersIntersectDates("2022-02-21", "2022-02-23");
        Assert.assertEquals(4, all.size());
    }
}
