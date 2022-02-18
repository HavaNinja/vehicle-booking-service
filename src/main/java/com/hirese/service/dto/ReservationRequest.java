package com.hirese.service.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class ReservationRequest {
    @NotEmpty
    private String startDate;
    @NotEmpty
    private String endDate;
    @NotNull
    private UUID userId;
    @NotEmpty
    private List<String> registrationNumbers;
}
