package com.example.demo.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Repository.IRecomendationRepository;
import com.example.demo.domain.Recomendation;
import com.example.demo.service.IRecomendationService;

@Service
public class RecomendationService implements IRecomendationService {

    @Autowired
    private IRecomendationRepository recomendationRepository;

    @Override
    public List<Recomendation> findAll() {
        return recomendationRepository.findAll();
    }

    @Override
    public Optional<Recomendation> findById(Integer id) {
        return recomendationRepository.findById(id);
    }

    @Override
    public Recomendation save(Recomendation recomendation) {
        return recomendationRepository.save(recomendation);
    }

    @Override
    public void delete(Integer id) {
        recomendationRepository.deleteById(id);
    }

    @Override
    public List<Recomendation> findByDescriptionContaining(String description) {
        return recomendationRepository.findByDescriptionContaining(description);
    }
}