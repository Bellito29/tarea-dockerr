package com.example.demo.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.domain.Stats;

@Repository
public interface IStatsRepository extends JpaRepository<Stats, Integer> {

    List<Stats> findByTimeGreaterThan(Long time);
    List<Stats> findByAmmountExercises(Integer ammountExercises);
    List<Stats> findByAmmountExercisesGreaterThan(Integer ammountExercises);
    List<Stats> findByUser_Id(Integer userId);
}