package com.example.demo.controller.rest.dto;

import lombok.Data;

@Data
public class ExerciseResponse {

    private Integer id;

    private String name;

    private String type;

    private String description;

    private Integer duration;

    private String difficulty;

    private String videoUrl;
}
