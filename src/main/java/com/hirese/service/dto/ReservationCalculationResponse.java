package com.hirese.service.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Map;

@Getter
@Setter
public class ReservationCalculationResponse {
    private BigDecimal price;
    private String starDate;
    private String endDate;
    private Map<String, String> cars;
}
