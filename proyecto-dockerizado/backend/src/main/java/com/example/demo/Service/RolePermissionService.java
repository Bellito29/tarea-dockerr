package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import com.example.demo.domain.RolePermission;

public interface RolePermissionService {

    List<RolePermission> findAll();

    RolePermission findById(Long id);

    RolePermission save(RolePermission rolePermission);

    RolePermission update(Long id, RolePermission rolePermission);

    void deleteById(Long id);

    List<RolePermission> findByRoleId(Long roleId);

    Optional<RolePermission> findByRoleIdAndPermissionId(Long roleId, Integer permissionId);

    boolean existsByRoleIdAndPermissionId(Long roleId, Integer permissionId);
}