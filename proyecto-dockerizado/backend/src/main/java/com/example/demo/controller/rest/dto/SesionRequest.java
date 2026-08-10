package com.example.demo.controller.rest.dto;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SesionRequest {

    private LocalDateTime sesionDate;

    private Long duration;
}
