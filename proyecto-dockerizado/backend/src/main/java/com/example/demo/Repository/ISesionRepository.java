package com.example.demo.Repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.domain.Sesion;

@Repository
public interface ISesionRepository extends JpaRepository<Sesion, Integer>{
    @EntityGraph(attributePaths = {"routines", "user"})
    List<Sesion> findAll()
    ;
    List<Sesion> findBySesionDate(LocalDateTime date);
    List<Sesion> findBySesionDateGreaterThan(LocalDateTime date);
    List<Sesion> findByDurationLessThan(Long duration);
}

