package com.example.demo.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.Admin;

@Repository
public interface IAdminRepository extends JpaRepository<Admin, Integer>{

    Optional<Admin> findById(Integer id);
    List<Admin> findAll();

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "DELETE FROM admins WHERE user_id = :id", nativeQuery = true)
    void deleteByUserId(@Param("id") Integer id);
}
