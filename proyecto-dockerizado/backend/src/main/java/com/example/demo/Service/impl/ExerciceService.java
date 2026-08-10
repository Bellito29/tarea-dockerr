package com.example.demo.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.Repository.IExerciceRepository;
import com.example.demo.domain.Exercise;
import com.example.demo.service.IExerciceService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ExerciceService implements IExerciceService{
    private final IExerciceRepository exerciceRepository;

    @Override
    public List<Exercise> findAll() {
        return exerciceRepository.findAll();
    }

    @Override
    public Optional<Exercise> findById(Integer id) {
        return exerciceRepository.findById(id);
    }

    @Override
    public Exercise save(Exercise exercise) {
        return exerciceRepository.save(exercise);
    }

    @Override
    public void delete(Integer id) {
        exerciceRepository.deleteById(id);
    }
}
