package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import com.example.demo.domain.Student;

public interface IStudentService {

    Student save(Student student);
    List<Student> findAll();
    Optional<Student> findById(Integer id);
    void delete(Integer id);

    List<Student> findByFullNameContaining(String fullName);
    List<Student> findByTrainerId(Integer trainerId);
    List<Student> findByWeightGreaterThan(Double weight);
    List<Student> findByBirthDate(String birthDate);
}