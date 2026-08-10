package com.example.demo.controller.rest.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExercisesInRoutineRequest {

    private Integer exerciseId;

    private Integer routineId;
}
