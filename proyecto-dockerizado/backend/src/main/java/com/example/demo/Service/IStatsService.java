package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import com.example.demo.domain.Stats;

public interface IStatsService {

    Stats save(Stats stats);
    List<Stats> findAll();
    Optional<Stats> findById(Integer id);
    void delete(Integer id);

    List<Stats> findByTimeGreaterThan(Long time);
    List<Stats> findByAmmountExercises(Integer ammountExercises);
    List<Stats> findByAmmountExercisesGreaterThan(Integer ammountExercises);
    List<Stats> findByUserId(Integer userId);
}