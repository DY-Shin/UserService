package com.ssafy.commonpjt.api.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class ReserveDto {
    private Long id;
    private LocalDate reserveDate;
    private LocalTime reserveTime;
}
