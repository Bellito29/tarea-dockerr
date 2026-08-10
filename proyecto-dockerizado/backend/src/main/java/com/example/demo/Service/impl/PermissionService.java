package com.example.demo.service.impl;

import org.springframework.stereotype.Service;

import com.example.demo.Repository.IPermissionRepository;
import com.example.demo.Repository.RolePermissionRepository;

import lombok.RequiredArgsConstructor;
import com.example.demo.domain.Permission;
import com.example.demo.domain.RolePermission;
import com.example.demo.service.IPermissionService;

import java.util.List;
import java.util.Optional;
import jakarta.transaction.Transactional;

@RequiredArgsConstructor
@Service
public class PermissionService implements IPermissionService {
    private final IPermissionRepository permissionRepository;
    private final RolePermissionRepository rolePermissionRepository;

    public Permission save(Permission permission){
        return permissionRepository.save(permission);
    }
    public List<Permission> findAll(){
        return permissionRepository.findAll();
    }
    public Optional<Permission> findById(Integer id){
        return permissionRepository.findById(id);
    }
    @Transactional
    public void deleteById(Integer id) {
        // 1. Eliminar RolePermissions que usen este permiso
        List<RolePermission> related = rolePermissionRepository.findAll().stream()
                .filter(rp -> rp.getPermission().getId().equals(id))
                .toList();

        if (!related.isEmpty()) {
            rolePermissionRepository.deleteAll(related);
        }

        // 2. Eliminar el permiso
        permissionRepository.deleteById(id);
    }
}
