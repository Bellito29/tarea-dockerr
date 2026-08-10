package com.example.demo.service;

import com.example.demo.domain.Permission;
import java.util.List;
import java.util.Optional;

public interface IPermissionService {
    public Permission save(Permission permission);
    public List<Permission> findAll();
    public Optional<Permission> findById(Integer id);
    public void deleteById(Integer id);
}
