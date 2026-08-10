package com.example.demo.controller.rest.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleRequest {

    private String name;

    private String description;

    private List<Integer> permissionIds;
}
