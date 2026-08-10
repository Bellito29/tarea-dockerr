package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import com.example.demo.domain.Trainer;

public interface ITrainerService {

    Trainer save(Trainer trainer);
    List<Trainer> findAll();
    Optional<Trainer> findById(Integer id);
    void delete(Integer id);

    List<Trainer> findByFullNameContaining(String fullName);
    List<Trainer> findByWeightGreaterThan(Double weight);
    List<Trainer> findByHeightGreaterThan(Double height);
    List<Trainer> findByBirthDate(String birthDate);
}