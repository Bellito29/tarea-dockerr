package com.example.demo.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.domain.Exercise;

@Repository
public interface IExerciceRepository extends JpaRepository<Exercise, Integer>{
    Optional<Exercise> findById(Integer id);
    List<Exercise> findAll();
}
