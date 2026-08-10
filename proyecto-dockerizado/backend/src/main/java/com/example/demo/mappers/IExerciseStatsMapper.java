package com.example.demo.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.demo.controller.rest.dto.ExerciseStatsRequest;
import com.example.demo.controller.rest.dto.ExerciseStatsResponse;
import com.example.demo.domain.ExerciseStats;

@Mapper(componentModel = "spring")
public interface IExerciseStatsMapper {

    @Mapping(source = "stats.id", target = "statsId")
    @Mapping(source = "exercise.id", target = "exerciseId")
    ExerciseStatsResponse exerciseStatsToResponse(ExerciseStats exerciseStats);

    List<ExerciseStatsResponse> exerciseStatsListToResponse(List<ExerciseStats> list);

    @Mapping(target = "stats", ignore = true)
    @Mapping(target = "exercise", ignore = true)
    ExerciseStats requestToExerciseStats(ExerciseStatsRequest request);
}
