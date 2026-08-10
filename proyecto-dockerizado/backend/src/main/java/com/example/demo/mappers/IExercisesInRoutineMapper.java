package com.example.demo.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.demo.controller.rest.dto.ExercisesInRoutineRequest;
import com.example.demo.controller.rest.dto.ExercisesInRoutineResponse;
import com.example.demo.domain.ExercisesInRoutine;

@Mapper(componentModel = "spring")
public interface IExercisesInRoutineMapper {

    @Mapping(source = "exercise.id", target = "exerciseId")
    @Mapping(source = "routine.id", target = "routineId")
    ExercisesInRoutineResponse exercisesInRoutineToResponse(ExercisesInRoutine exercisesInRoutine);

    List<ExercisesInRoutineResponse> exercisesInRoutineListToResponse(List<ExercisesInRoutine> list);

    @Mapping(target = "exercise", ignore = true)
    @Mapping(target = "routine", ignore = true)
    ExercisesInRoutine requestToExercisesInRoutine(ExercisesInRoutineRequest request);
}
