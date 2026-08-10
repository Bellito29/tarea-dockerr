package com.example.demo.controller.rest.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class EventRequest {
    
    private String title;

    private String place;

    private LocalDateTime date;
}
