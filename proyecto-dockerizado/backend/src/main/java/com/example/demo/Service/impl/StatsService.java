package com.example.demo.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.Repository.IStatsRepository;
import com.example.demo.domain.Stats;
import com.example.demo.service.IStatsService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class StatsService implements IStatsService {

    private final IStatsRepository statsRepository;

    @Override
    public Stats save(Stats stats) {
        return statsRepository.save(stats);
    }

    @Override
    public List<Stats> findAll() {
        return statsRepository.findAll();
    }

    @Override
    public Optional<Stats> findById(Integer id) {
        return statsRepository.findById(id);
    }

    @Override
    public void delete(Integer id) {
        statsRepository.deleteById(id);
    }

    @Override
    public List<Stats> findByTimeGreaterThan(Long time) {
        return statsRepository.findByTimeGreaterThan(time);
    }

    @Override
    public List<Stats> findByAmmountExercises(Integer ammountExercises) {
        return statsRepository.findByAmmountExercises(ammountExercises);
    }

    @Override
    public List<Stats> findByAmmountExercisesGreaterThan(Integer ammountExercises) {
        return statsRepository.findByAmmountExercisesGreaterThan(ammountExercises);
    }

    @Override
    public List<Stats> findByUserId(Integer userId) {
        return statsRepository.findByUser_Id(userId);
    }
}