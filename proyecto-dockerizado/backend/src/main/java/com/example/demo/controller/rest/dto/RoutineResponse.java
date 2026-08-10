package com.example.demo.controller.rest.dto;

import lombok.Data;

@Data
public class RoutineResponse {

    private Integer id;

    private String routineName;

    private String description;

    private String visibility;
}
