package com.example.demo.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.demo.controller.rest.dto.RolePermissionRequest;
import com.example.demo.controller.rest.dto.RolePermissionResponse;
import com.example.demo.domain.RolePermission;

@Mapper(componentModel = "spring")
public interface IRolePermissionMapper {

    @Mapping(source = "role.id", target = "roleId")
    @Mapping(source = "permission.id", target = "permissionId")
    RolePermissionResponse rolePermissionToRolePermissionResponse(RolePermission rolePermission);

    List<RolePermissionResponse> rolePermissionsToRolePermissionsResponse(List<RolePermission> rolePermissions);

    @Mapping(target = "role", ignore = true)
    @Mapping(target = "permission", ignore = true)
    RolePermission rolePermissionRequestToRolePermission(RolePermissionRequest rolePermissionRequest);
}
