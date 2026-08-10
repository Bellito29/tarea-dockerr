package com.example.demo.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.Repository.ITrainerRepository;
import com.example.demo.domain.Trainer;
import com.example.demo.service.ITrainerService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class TrainerService implements ITrainerService {

    private final ITrainerRepository trainerRepository;

    @Override
    public Trainer save(Trainer trainer) {
        return trainerRepository.save(trainer);
    }

    @Override
    public List<Trainer> findAll() {
        return trainerRepository.findAll();
    }

    @Override
    public Optional<Trainer> findById(Integer id) {
        return trainerRepository.findById(id);
    }

    @Override
    public void delete(Integer id) {
        trainerRepository.deleteById(id);
    }

    @Override
    public List<Trainer> findByFullNameContaining(String fullName) {
        return trainerRepository.findByFullNameContaining(fullName);
    }

    @Override
    public List<Trainer> findByWeightGreaterThan(Double weight) {
        return trainerRepository.findByWeightGreaterThan(weight);
    }

    @Override
    public List<Trainer> findByHeightGreaterThan(Double height) {
        return trainerRepository.findByHeightGreaterThan(height);
    }

    @Override
    public List<Trainer> findByBirthDate(String birthDate) {
        return trainerRepository.findByBirthDate(birthDate);
    }
}