package com.example.demo.service;

import java.util.List;

import com.example.demo.domain.Role;

public interface RoleService {

    List<Role> findAll();

    Role findById(Long id);

    Role save(Role role);

    Role update(Long id, Role role);

    void deleteById(Long id);

    
}
