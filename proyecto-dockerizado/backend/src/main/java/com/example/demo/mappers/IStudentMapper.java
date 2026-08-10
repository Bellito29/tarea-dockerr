package com.example.demo.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import com.example.demo.controller.rest.dto.StudentRequest;
import com.example.demo.controller.rest.dto.StudentResponse;
import com.example.demo.domain.Student;

@Mapper(componentModel = "spring")
public interface IStudentMapper {

    StudentResponse studentToStudentResponse(Student student);

    List<StudentResponse> studentsToStudentsResponse(List<Student> students);

    Student studentRequestToStudent(StudentRequest studentRequest);
}
