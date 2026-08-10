package com.example.demo.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.demo.domain.Permission;
import java.util.List;
import java.util.Optional;

@Repository
public interface IPermissionRepository extends JpaRepository<Permission, Integer>{
    public List<Permission> findAll();
    public Optional<Permission> findById(Integer Id);
    public void deleteById(Integer Id);
}
