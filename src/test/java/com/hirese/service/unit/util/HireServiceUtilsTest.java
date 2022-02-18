package com.hirese.service.unit.util;

import com.hirese.service.util.HireServiceUtils;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;

public class HireServiceUtilsTest {

    @Test
    public void shouldReturnTrueWhenDateIsInPast() {
        Assert.assertTrue(HireServiceUtils.isDateInPast("2019-02-21"));
    }

    @Test
    public void shouldReturnFalseWhenDateIsInFuture() {
        Assert.assertFalse(HireServiceUtils.isDateInPast(LocalDate.now().toString()));
    }

    @Test
    public void shouldReturnTwoWhenPassTodayAndTomorrow() {
        final String startDate = LocalDate.now().toString();
        final String endDate = LocalDate.now().plusDays(1L).toString();
        Assert.assertEquals(2L, HireServiceUtils.daysBetweenRangeIncluded(startDate, endDate));
    }
}