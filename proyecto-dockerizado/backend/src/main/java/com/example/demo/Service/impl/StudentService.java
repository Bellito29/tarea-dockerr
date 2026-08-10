package com.example.demo.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.Repository.IStudentRepository;
import com.example.demo.domain.Student;
import com.example.demo.service.IStudentService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class StudentService implements IStudentService {

    private final IStudentRepository studentRepository;

    @Override
    public Student save(Student student) {
        return studentRepository.save(student);
    }

    @Override
    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    @Override
    public Optional<Student> findById(Integer id) {
        return studentRepository.findById(id);
    }

    @Override
    public void delete(Integer id) {
        studentRepository.deleteById(id);
    }

    @Override
    public List<Student> findByFullNameContaining(String fullName) {
        return studentRepository.findByFullNameContaining(fullName);
    }

    @Override
    public List<Student> findByTrainerId(Integer trainerId) {
        return studentRepository.findByTrainer_Id(trainerId);
    }

    @Override
    public List<Student> findByWeightGreaterThan(Double weight) {
        return studentRepository.findByWeightGreaterThan(weight);
    }

    @Override
    public List<Student> findByBirthDate(String birthDate) {
        return studentRepository.findByBirthDate(birthDate);
    }
}