package com.example.demo.controller.rest.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class SesionResponse {

    private Integer id;
    private LocalDateTime sesionDate;
    private Long duration;

    // campos nuevos
    private String userName;
    private String routineName;
    private List<String> exercises;
}
