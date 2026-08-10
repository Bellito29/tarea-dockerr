package com.example.demo.controller.rest.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class UserProgressResponse {

    private Integer id;

    private LocalDateTime progressDate;

    private String effortLevel;

    private String notes;

    private Integer pastWeight;

    private Integer currentWeight;
}
