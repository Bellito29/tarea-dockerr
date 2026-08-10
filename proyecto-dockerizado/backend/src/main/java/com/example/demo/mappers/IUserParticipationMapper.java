package com.example.demo.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.demo.controller.rest.dto.UserParticipationRequest;
import com.example.demo.controller.rest.dto.UserParticipationResponse;
import com.example.demo.domain.UserParticipation;

@Mapper(componentModel = "spring")
public interface IUserParticipationMapper {

    @Mapping(source = "event.id", target = "eventId")
    @Mapping(source = "user.id", target = "userId")
    UserParticipationResponse userParticipationToResponse(UserParticipation userParticipation);

    List<UserParticipationResponse> userParticipationsToResponse(List<UserParticipation> list);

    @Mapping(target = "event", ignore = true)
    @Mapping(target = "user", ignore = true)
    UserParticipation requestToUserParticipation(UserParticipationRequest request);
}
