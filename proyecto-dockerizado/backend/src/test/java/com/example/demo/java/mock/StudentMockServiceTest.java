package com.example.demo.java.mock;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import com.example.demo.Repository.IStudentRepository;
import com.example.demo.domain.Student;
import com.example.demo.domain.Trainer;
import com.example.demo.service.impl.StudentService;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class StudentMockServiceTest {

    @Mock
    private IStudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    private Trainer trainer;
    private Student student1;
    private Student student2;
    private Student student3;

    @BeforeEach
    public void setUp() {
        trainer = new Trainer();
        trainer.setId(10);
        trainer.setFullName("Trainer Mock");
        trainer.setPassword("123");
        trainer.setBirthDate("1990-01-01");
        trainer.setWeight(80.0);
        trainer.setHeight(1.80);

        student1 = new Student();
        student1.setId(1);
        student1.setFullName("Carlos Diaz");
        student1.setPassword("123");
        student1.setBirthDate("2000-01-01");
        student1.setWeight(60.0);
        student1.setHeight(1.70);
        student1.setTrainer(trainer);

        student2 = new Student();
        student2.setId(2);
        student2.setFullName("Carlos Perez");
        student2.setPassword("123");
        student2.setBirthDate("2000-01-01");
        student2.setWeight(72.0);
        student2.setHeight(1.72);
        student2.setTrainer(trainer);

        student3 = new Student();
        student3.setId(3);
        student3.setFullName("Laura Gomez");
        student3.setPassword("123");
        student3.setBirthDate("2002-01-01");
        student3.setWeight(80.0);
        student3.setHeight(1.75);
        student3.setTrainer(trainer);
    }

    @Test
    public void testFindAll() {
        when(studentRepository.findAll()).thenReturn(Arrays.asList(student1, student2, student3));

        List<Student> result = studentService.findAll();

        assertNotNull(result);
        assertEquals(3, result.size());
        verify(studentRepository, times(1)).findAll();
    }

    @Test
    public void testFindById() {
        when(studentRepository.findById(1)).thenReturn(Optional.of(student1));

        Optional<Student> result = studentService.findById(1);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().getId());
        verify(studentRepository, times(1)).findById(1);
    }

    @Test
    public void testFindByIdNotFound() {
        when(studentRepository.findById(99)).thenReturn(Optional.empty());

        Optional<Student> result = studentService.findById(99);

        assertFalse(result.isPresent());
        verify(studentRepository, times(1)).findById(99);
    }

    @Test
    public void testSave() {
        when(studentRepository.save(any(Student.class))).thenReturn(student1);

        Student result = studentService.save(student1);

        assertNotNull(result);
        assertEquals(1, result.getId());
        verify(studentRepository, times(1)).save(any(Student.class));
    }

    @Test
    public void testDelete() {
        doNothing().when(studentRepository).deleteById(1);

        studentService.delete(1);

        verify(studentRepository, times(1)).deleteById(1);
    }

    @Test
    public void testFindByFullNameContaining() {
        when(studentRepository.findByFullNameContaining("Carlos")).thenReturn(Arrays.asList(student1, student2));

        List<Student> result = studentService.findByFullNameContaining("Carlos");

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(studentRepository, times(1)).findByFullNameContaining("Carlos");
    }

    @Test
    public void testFindByTrainerId() {
        when(studentRepository.findByTrainer_Id(10)).thenReturn(Arrays.asList(student1, student2, student3));

        List<Student> result = studentService.findByTrainerId(10);

        assertNotNull(result);
        assertEquals(3, result.size());
        verify(studentRepository, times(1)).findByTrainer_Id(10);
    }

    @Test
    public void testFindByWeightGreaterThan() {
        when(studentRepository.findByWeightGreaterThan(65.0)).thenReturn(Arrays.asList(student2, student3));

        List<Student> result = studentService.findByWeightGreaterThan(65.0);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(studentRepository, times(1)).findByWeightGreaterThan(65.0);
    }

    @Test
    public void testFindByBirthDate() {
        when(studentRepository.findByBirthDate("2000-01-01")).thenReturn(Arrays.asList(student1, student2));

        List<Student> result = studentService.findByBirthDate("2000-01-01");

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(studentRepository, times(1)).findByBirthDate("2000-01-01");
    }
}