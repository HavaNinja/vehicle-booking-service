package com.hirese.service.util;

import com.hirese.service.dto.GenericJsonResponse;
import com.hirese.service.dto.GenericJsonResponseList;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static java.time.temporal.ChronoUnit.DAYS;

public class HireServiceUtils {
    private HireServiceUtils() {
    }

    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static <T> GenericJsonResponseList<T> wrapResponseList(List<T> data) {
        return new GenericJsonResponseList<>(data);
    }

    public static <T> GenericJsonResponse<T> wrapResponse(T data) {
        return new GenericJsonResponse<>(data);
    }

    public static boolean isDateInPast(final String date) {
        return LocalDate.parse(date).isBefore(LocalDate.now());
    }

    public static long daysBetweenRangeIncluded(final String startDate, final String endDate) {
        return DAYS.between(LocalDate.parse(startDate, FORMATTER), LocalDate.parse(endDate, FORMATTER)) + 1;
    }
}
