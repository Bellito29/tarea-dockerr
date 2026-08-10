package com.example.demo.controller.rest.dto;

import lombok.Data;

@Data
public class UserResponse {

    private int id;

    private String fullName;

    private String birthDate;

    private String email;

    private String roleName;

}
