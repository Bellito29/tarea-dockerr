package com.example.demo.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.Trainer;

@Repository
public interface ITrainerRepository extends JpaRepository<Trainer, Integer> {

    List<Trainer> findByFullNameContaining(String fullName);
    List<Trainer> findByWeightGreaterThan(Double weight);
    List<Trainer> findByHeightGreaterThan(Double height);
    List<Trainer> findByBirthDate(String birthDate);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "DELETE FROM trainers WHERE user_id = :id", nativeQuery = true)
    void deleteByUserId(@Param("id") Integer id);
}