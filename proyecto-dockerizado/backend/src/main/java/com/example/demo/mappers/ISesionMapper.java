package com.example.demo.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import com.example.demo.controller.rest.dto.SesionRequest;
import com.example.demo.controller.rest.dto.SesionResponse;
import com.example.demo.domain.Sesion;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import com.example.demo.domain.Routine;

@Mapper(componentModel = "spring")
public interface ISesionMapper {

    @Mapping(source = "user.fullName", target = "userName")
    @Mapping(source = "routines", target = "routineName", qualifiedByName = "firstRoutineName")
    @Mapping(target = "exercises", ignore = true)
    SesionResponse sesionToSesionResponse(Sesion sesion);

    List<SesionResponse> sesionsToSesionsResponse(List<Sesion> sesions);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "routines", ignore = true)
    @Mapping(target = "user", ignore = true)
    Sesion sesionRequestToSesion(SesionRequest sesionRequest);

    @Named("firstRoutineName")
    default String firstRoutineName(List<Routine> routines) {
        if (routines == null || routines.isEmpty()) return null;
        return routines.get(0).getRoutineName();
    }
}