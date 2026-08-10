package com.example.demo.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import com.example.demo.controller.rest.dto.UserRequest;
import com.example.demo.controller.rest.dto.UserResponse;
import com.example.demo.domain.User;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IUserMapper {
    
    @Mapping(source = "role.name", target = "roleName")  // ← consistente
    UserResponse userToUserResponse(User user);

    List<UserResponse> usersToUsersResponse(List<User> users);  // ← no 

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "userRecomendations", ignore = true)
    @Mapping(target = "progress", ignore = true)
    @Mapping(target = "stats", ignore = true)
    @Mapping(target = "sesions", ignore = true)
    @Mapping(target = "recievedMessages", ignore = true)
    @Mapping(target = "sentMessages", ignore = true)
    @Mapping(target = "notifications", ignore = true)
    @Mapping(target = "userParticipations", ignore = true)
    @Mapping(target = "role", ignore = true)
    User userRequestToUser(UserRequest userRequest);

}
