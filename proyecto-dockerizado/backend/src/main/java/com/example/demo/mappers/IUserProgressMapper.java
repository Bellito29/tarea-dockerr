package com.example.demo.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import com.example.demo.controller.rest.dto.UserProgressRequest;
import com.example.demo.controller.rest.dto.UserProgressResponse;
import com.example.demo.domain.UserProgress;

@Mapper(componentModel = "spring")
public interface IUserProgressMapper {

    UserProgressResponse userProgressToResponse(UserProgress userProgress);

    List<UserProgressResponse> userProgressListToResponse(List<UserProgress> list);

    UserProgress requestToUserProgress(UserProgressRequest request);
}
