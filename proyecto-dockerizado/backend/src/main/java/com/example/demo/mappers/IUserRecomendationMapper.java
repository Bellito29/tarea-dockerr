package com.example.demo.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.demo.controller.rest.dto.UserRecomendationRequest;
import com.example.demo.controller.rest.dto.UserRecomendationResponse;
import com.example.demo.domain.UserRecomendation;

@Mapper(componentModel = "spring")
public interface IUserRecomendationMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "recomendation.id", target = "recomendationId")
    UserRecomendationResponse userRecomendationToResponse(UserRecomendation userRecomendation);

    List<UserRecomendationResponse> userRecomendationsToResponse(List<UserRecomendation> list);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "recomendation", ignore = true)
    UserRecomendation requestToUserRecomendation(UserRecomendationRequest request);
}
