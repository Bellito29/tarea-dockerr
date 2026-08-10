package com.example.demo.controller.rest.dto;

import lombok.Data;

@Data
public class PermissionResponse {

    private Integer id;

    private Integer code;

    private String name;

    private String description;
}
