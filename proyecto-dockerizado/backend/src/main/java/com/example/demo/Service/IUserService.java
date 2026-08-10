package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import com.example.demo.controller.rest.dto.UserRequest;
import com.example.demo.domain.User;

public interface IUserService {
    User save(UserRequest request);
    User saveUser(User user);
    User update(Integer id, UserRequest request);
    List<User> findAll();
    Optional<User> findById(Integer id);
    void delete(Integer id);
    Optional<User> findByFullName(String fullName);
    List<User> findByFullNameContaining(String fullName);
    void unassignRole(Integer userId);
    User assignRole(Integer userId, Long roleId);
}