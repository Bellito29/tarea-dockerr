package com.example.demo.mappers;

import java.util.ArrayList;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.demo.controller.rest.dto.PermissionResponse;
import com.example.demo.controller.rest.dto.RoleRequest;
import com.example.demo.controller.rest.dto.RoleResponse;
import com.example.demo.domain.Role;
import com.example.demo.domain.RolePermission;
import com.example.demo.domain.Permission;




@Mapper(componentModel = "spring")
public interface IRoleMapper {

    @Mapping(source = "rolePermissions", target = "permissions")
    RoleResponse roleToRoleResponse(Role role);

    List<RoleResponse> rolesToRolesResponse(List<Role> roles);
    
    default Role roleRequestToRole(RoleRequest roleRequest) {
        if (roleRequest == null) return null;
        Role role = new Role();
        role.setName(roleRequest.getName());
        role.setDescription(roleRequest.getDescription());

        List<RolePermission> rolePermissions = new ArrayList<>();
        if (roleRequest.getPermissionIds() != null) {
            for (Integer permId : roleRequest.getPermissionIds()) {
                RolePermission rp = new RolePermission();
                Permission p = new Permission();
                p.setId(permId);
                rp.setPermission(p);
                rp.setRole(role);
                rolePermissions.add(rp);
            }
        }
        role.setRolePermissions(rolePermissions);
        return role;
    }

    // Este método toma cada objeto de la lista intermedia y extrae el permiso real
    // que ahora SÍ vendrá lleno gracias al EntityGraph del repositorio
    default PermissionResponse mapRolePermissionToPermissionResponse(RolePermission rolePermission) {
        if (rolePermission == null || rolePermission.getPermission() == null) {
            return null;
        }
        PermissionResponse res = new PermissionResponse();
        res.setId(rolePermission.getPermission().getId());
        res.setName(rolePermission.getPermission().getName());
        res.setCode(rolePermission.getPermission().getCode());
        return res;
    }
}
