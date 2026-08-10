package com.example.demo.controller.rest.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoutineRequest {

    private String routineName;

    private String description;

    private String visibility;
}
