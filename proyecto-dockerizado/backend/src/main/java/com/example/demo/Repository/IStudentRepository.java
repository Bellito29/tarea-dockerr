package com.example.demo.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.Student;

@Repository
public interface IStudentRepository extends JpaRepository<Student, Integer> {

    List<Student> findByFullNameContaining(String fullName);
    List<Student> findByTrainer_Id(Integer trainerId);
    List<Student> findByWeightGreaterThan(Double weight);
    List<Student> findByBirthDate(String birthDate);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "DELETE FROM students WHERE user_id = :id", nativeQuery = true)
    void deleteByUserId(@Param("id") Integer id);
}