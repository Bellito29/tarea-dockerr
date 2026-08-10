package com.example.demo.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import com.example.demo.controller.rest.dto.RecomendationRequest;
import com.example.demo.controller.rest.dto.RecomendationResponse;
import com.example.demo.domain.Recomendation;

@Mapper(componentModel = "spring")
public interface IRecomendationMapper {

    RecomendationResponse recomendationToRecomendationResponse(Recomendation recomendation);

    List<RecomendationResponse> recomendationsToRecomendationsResponse(List<Recomendation> recomendations);

    Recomendation recomendationRequestToRecomendation(RecomendationRequest recomendationRequest);
}
