package com.example.demo.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.domain.User;

@Repository
public interface IUserRepository extends JpaRepository<User, Integer> {

    @EntityGraph(attributePaths = {"progress", "role", "role.rolePermissions"})
    List<User> findAll();

    Optional<User> findByFullName(String fullName);
    List<User> findByFullNameContaining(String fullName);
    
    @EntityGraph(attributePaths = {"role"})
    List<User> findByRoleId(Long roleId);
}