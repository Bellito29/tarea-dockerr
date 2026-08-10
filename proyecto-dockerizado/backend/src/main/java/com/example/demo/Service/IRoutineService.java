package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import com.example.demo.domain.Routine;

public interface IRoutineService {
    Routine save(Routine routine);
    List<Routine> findAll();
    Optional<Routine> findById(Integer id);
    void delete(Integer id);

    List<Routine> findByRoutineName(String routineName);
    List<Routine> findByRoutineNameContaining(String name);
    List<Routine> findByVisibility(Boolean visibility);
    List<Routine> findByDescriptionContaining(String description);
}