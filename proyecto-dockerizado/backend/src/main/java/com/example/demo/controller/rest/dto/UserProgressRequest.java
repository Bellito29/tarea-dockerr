package com.example.demo.controller.rest.dto;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProgressRequest {

    private LocalDateTime progressDate;

    private String effortLevel;

    private String notes;

    private Integer pastWeight;

    private Integer currentWeight;
}
