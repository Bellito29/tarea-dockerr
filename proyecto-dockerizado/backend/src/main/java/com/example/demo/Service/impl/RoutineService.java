package com.example.demo.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Repository.IRoutineRepository;
import com.example.demo.domain.Routine;
import com.example.demo.service.IRoutineService;

@Service
public class RoutineService implements IRoutineService {

    @Autowired
    private IRoutineRepository routineRepository;

    @Override
    public Routine save(Routine routine) {
        return routineRepository.save(routine);
    }

    @Override
    public List<Routine> findAll() {
        return routineRepository.findAll();
    }

    @Override
    public Optional<Routine> findById(Integer id) {
        return routineRepository.findById(id);
    }

    @Override
    public void delete(Integer id) {
        routineRepository.deleteById(id);
    }

    @Override
    public List<Routine> findByRoutineName(String routineName) {
        return routineRepository.findByRoutineName(routineName);
    }

    @Override
    public List<Routine> findByRoutineNameContaining(String name) {
        return routineRepository.findByRoutineNameContaining(name);
    }

    @Override
    public List<Routine> findByVisibility(Boolean visibility) {
        return routineRepository.findByVisibility(visibility);
    }

    @Override
    public List<Routine> findByDescriptionContaining(String description) {
        return routineRepository.findByDescriptionContaining(description);
    }
}