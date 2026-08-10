package com.example.demo.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.demo.controller.rest.dto.PermissionRequest;
import com.example.demo.controller.rest.dto.PermissionResponse;
import com.example.demo.domain.Permission;

@Mapper(componentModel = "spring")
public interface IPermissionMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "code", target = "code")
    PermissionResponse permissionToPermissionResponse(Permission permission);

    List<PermissionResponse> permissionsToPermissionsResponse(List<Permission> permissions);

    
    Permission permissionRequestToPermission(PermissionRequest permissionRequest);
}
