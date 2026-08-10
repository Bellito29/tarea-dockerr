package com.example.demo.controller.rest.dto;

import lombok.Data;

@Data
public class UserRecomendationResponse {

    private Integer id;

    private Integer userId;

    private Integer recomendationId;
}
