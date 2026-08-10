package com.example.demo.controller.rest.dto;

import lombok.Data;

@Data
public class RolePermissionResponse {

    private Long id;

    private Long roleId;

    private Integer permissionId;
}
