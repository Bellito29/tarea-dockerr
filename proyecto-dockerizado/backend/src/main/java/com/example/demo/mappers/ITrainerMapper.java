package com.example.demo.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import com.example.demo.controller.rest.dto.TrainerRequest;
import com.example.demo.controller.rest.dto.TrainerResponse;
import com.example.demo.domain.Trainer;

@Mapper(componentModel = "spring")
public interface ITrainerMapper {

    TrainerResponse trainerToTrainerResponse(Trainer trainer);

    List<TrainerResponse> trainersToTrainersResponse(List<Trainer> trainers);

    Trainer trainerRequestToTrainer(TrainerRequest trainerRequest);
}
