package com.example.demo.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.Repository.RolePermissionRepository;
import com.example.demo.domain.RolePermission;
import com.example.demo.service.RolePermissionService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class RolePermissionServiceImpl implements RolePermissionService {

    private final RolePermissionRepository rolePermissionRepository;

    public RolePermissionServiceImpl(RolePermissionRepository rolePermissionRepository) {
        this.rolePermissionRepository = rolePermissionRepository;
    }

    @Override
    public List<RolePermission> findAll() {
        return rolePermissionRepository.findAll();
    }

    @Override
    public RolePermission findById(Long id) {
        return rolePermissionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("RolePermission not found with id: " + id));
    }

    @Override
    public RolePermission save(RolePermission rolePermission) {
        return rolePermissionRepository.save(rolePermission);
    }

    @Override
    public RolePermission update(Long id, RolePermission rolePermission) {
        findById(id);
        rolePermission.setId(id);
        return rolePermissionRepository.save(rolePermission);
    }

    @Override
    public void deleteById(Long id) {
        RolePermission rolePermission = findById(id);
        rolePermissionRepository.delete(rolePermission);
    }

    @Override
    public List<RolePermission> findByRoleId(Long roleId) {
        return rolePermissionRepository.findByRoleId(roleId);
    }

    @Override
    public Optional<RolePermission> findByRoleIdAndPermissionId(Long roleId, Integer permissionId) {
        return rolePermissionRepository.findByRoleIdAndPermissionId(roleId, permissionId);
    }

    @Override
    public boolean existsByRoleIdAndPermissionId(Long roleId, Integer permissionId) {
        return rolePermissionRepository.existsByRoleIdAndPermissionId(roleId, permissionId);
    }
}