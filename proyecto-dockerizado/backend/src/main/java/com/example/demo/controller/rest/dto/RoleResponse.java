package com.example.demo.controller.rest.dto;

import lombok.Data;
import java.util.List;

@Data
public class RoleResponse {

    private Long id;

    private String name;

    private String description;

    private List<PermissionResponse> permissions;
}
