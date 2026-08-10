package com.example.demo.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.domain.Recomendation;

@Repository
public interface IRecomendationRepository extends JpaRepository<Recomendation, Integer>{
    
    List<Recomendation> findByDescriptionContaining(String description);

}
