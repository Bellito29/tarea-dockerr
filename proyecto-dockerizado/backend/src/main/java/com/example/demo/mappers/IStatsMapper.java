package com.example.demo.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import com.example.demo.controller.rest.dto.StatsRequest;
import com.example.demo.controller.rest.dto.StatsResponse;
import com.example.demo.domain.Stats;

@Mapper(componentModel = "spring")
public interface IStatsMapper {

    StatsResponse statsToStatsResponse(Stats stats);

    List<StatsResponse> statsListToStatsResponse(List<Stats> statsList);

    Stats statsRequestToStats(StatsRequest statsRequest);
}
