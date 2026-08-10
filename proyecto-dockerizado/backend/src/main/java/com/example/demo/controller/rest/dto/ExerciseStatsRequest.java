package com.example.demo.controller.rest.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExerciseStatsRequest {

    private Integer statsId;

    private Integer exerciseId;
}
