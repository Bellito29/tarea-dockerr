package com.example.demo.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import com.example.demo.controller.rest.dto.ExerciseRequest;
import com.example.demo.controller.rest.dto.ExerciseResponse;
import com.example.demo.domain.Exercise;

@Mapper(componentModel = "spring")
public interface IExerciseMapper {

    ExerciseResponse exerciseToExerciseResponse(Exercise exercise);

    List<ExerciseResponse> exercisesToExercisesResponse(List<Exercise> exercises);

    Exercise exerciseRequestToExercise(ExerciseRequest exerciseRequest);
}
