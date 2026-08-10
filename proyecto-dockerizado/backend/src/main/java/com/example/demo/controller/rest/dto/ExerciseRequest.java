package com.example.demo.controller.rest.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExerciseRequest {

    private String name;

    private String type;

    private String description;

    private Integer duration;

    private String difficulty;

    private String videoUrl;
}
