package com.hirese.service.dto;


import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Getter
@Setter
public class ReservationCalculationRequest {
    @NotEmpty
    private List<String> registrationNumbers;
    @NotEmpty
    private String startDate;
    @NotEmpty
    private String endDate;
}
