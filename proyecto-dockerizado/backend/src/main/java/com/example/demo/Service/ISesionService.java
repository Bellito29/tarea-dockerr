package com.example.demo.service;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.example.demo.domain.Sesion;

public interface ISesionService {

    Sesion save(Sesion sesion);
    List<Sesion> findAll();
    Optional<Sesion> findById(Integer id);
    void delete(Integer id);

    List<Sesion> findBySesionDate(LocalDateTime date);
    List<Sesion> findBySesionDateGreaterThan(LocalDateTime date);
    List<Sesion> findByDurationLessThan(Long duration);
}