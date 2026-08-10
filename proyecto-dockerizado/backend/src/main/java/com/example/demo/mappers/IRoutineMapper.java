package com.example.demo.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import com.example.demo.controller.rest.dto.RoutineRequest;
import com.example.demo.controller.rest.dto.RoutineResponse;
import com.example.demo.domain.Routine;

@Mapper(componentModel = "spring")
public interface IRoutineMapper {

    RoutineResponse routineToRoutineResponse(Routine routine);

    List<RoutineResponse> routinesToRoutinesResponse(List<Routine> routines);

    Routine routineRequestToRoutine(RoutineRequest routineRequest);
}
