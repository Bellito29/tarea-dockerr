package com.example.demo.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.domain.Routine;

@Repository
public interface IRoutineRepository extends JpaRepository<Routine, Integer>{
    
    List<Routine> findByRoutineName(String routineName);
    List<Routine> findByRoutineNameContaining(String name);
    List<Routine> findByVisibility(Boolean visibility);
    List<Routine> findByDescriptionContaining(String description);
}
