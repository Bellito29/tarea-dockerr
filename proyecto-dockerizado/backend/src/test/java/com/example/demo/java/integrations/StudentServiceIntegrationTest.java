package com.example.demo.java.integrations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.example.demo.domain.Student;
import com.example.demo.domain.Trainer;
import com.example.demo.service.IStudentService;
import com.example.demo.service.ITrainerService;

import jakarta.transaction.Transactional;

@SpringBootTest
@ActiveProfiles("demo")
@Transactional
public class StudentServiceIntegrationTest {

    @Autowired
    private IStudentService studentService;

    @Autowired
    private ITrainerService trainerService;

    private Trainer buildValidTrainer(String name) {
        Trainer trainer = new Trainer();
        trainer.setFullName(name);
        trainer.setBirthDate("1990-01-01");
        trainer.setWeight(80.0);
        trainer.setHeight(1.80);
        trainer.setPassword("123");
        return trainer;
    }

    private Student buildValidStudent(String name, String birthDate, Double weight, Trainer trainer) {
        Student student = new Student();
        student.setFullName(name);
        student.setBirthDate(birthDate);
        student.setWeight(weight);
        student.setHeight(1.70);
        student.setPassword("123");
        student.setTrainer(trainer);
        return student;
    }

    @Test
    public void testSave() {
        Trainer trainer = trainerService.save(buildValidTrainer("Trainer Save Student"));
        Student saved = studentService.save(buildValidStudent("Student Save", "2001-01-01", 65.0, trainer));

        assertNotNull(saved);
        assertNotNull(saved.getId());
    }

    @Test
    public void testFindAll() {
        int initialSize = studentService.findAll().size();

        Trainer trainer = trainerService.save(buildValidTrainer("Trainer FindAll Student"));
        studentService.save(buildValidStudent("Student FindAll A", "2001-01-01", 65.0, trainer));
        studentService.save(buildValidStudent("Student FindAll B", "2002-01-01", 66.0, trainer));

        List<Student> result = studentService.findAll();

        assertNotNull(result);
        assertEquals(initialSize + 2, result.size());
    }

    @Test
    public void testFindById() {
        Trainer trainer = trainerService.save(buildValidTrainer("Trainer FindById Student"));
        Student saved = studentService.save(buildValidStudent("Student FindById", "2001-01-01", 67.0, trainer));

        Optional<Student> result = studentService.findById(saved.getId());

        assertTrue(result.isPresent());
        assertEquals(saved.getId(), result.get().getId());
    }

    @Test
    public void testFindById_NotFound() {
        Optional<Student> result = studentService.findById(999999);
        assertFalse(result.isPresent());
    }

    @Test
    public void testDelete() {
        Trainer trainer = trainerService.save(buildValidTrainer("Trainer Delete Student"));
        Student saved = studentService.save(buildValidStudent("Student Delete", "2001-01-01", 68.0, trainer));
        Integer id = saved.getId();

        studentService.delete(id);

        Optional<Student> result = studentService.findById(id);
        assertFalse(result.isPresent());
    }

    @Test
    public void testFindByFullNameContaining() {
        String token = "ITEST_STUDENT_TOKEN";
        int initialSize = studentService.findByFullNameContaining(token).size();

        Trainer trainer = trainerService.save(buildValidTrainer("Trainer Name Student"));
        studentService.save(buildValidStudent(token + "_A", "2001-01-01", 60.0, trainer));
        studentService.save(buildValidStudent(token + "_B", "2002-01-01", 61.0, trainer));
        studentService.save(buildValidStudent("OTHER_STUDENT_NAME", "2003-01-01", 62.0, trainer));

        List<Student> result = studentService.findByFullNameContaining(token);

        assertNotNull(result);
        assertEquals(initialSize + 2, result.size());
    }

    @Test
    public void testFindByTrainerId() {
        Trainer trainerOne = trainerService.save(buildValidTrainer("Trainer One Students"));
        Trainer trainerTwo = trainerService.save(buildValidTrainer("Trainer Two Students"));

        studentService.save(buildValidStudent("Student A", "2001-01-01", 63.0, trainerOne));
        studentService.save(buildValidStudent("Student B", "2002-01-01", 64.0, trainerOne));
        studentService.save(buildValidStudent("Student C", "2003-01-01", 65.0, trainerTwo));

        List<Student> result = studentService.findByTrainerId(trainerOne.getId());

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(s -> s.getTrainer().getId().equals(trainerOne.getId())));
    }

    @Test
    public void testFindByWeightGreaterThan() {
        int initialSize = studentService.findByWeightGreaterThan(150.0).size();

        Trainer trainer = trainerService.save(buildValidTrainer("Trainer Weight Student"));
        studentService.save(buildValidStudent("Student Heavy A", "2001-01-01", 160.0, trainer));
        studentService.save(buildValidStudent("Student Heavy B", "2002-01-01", 170.0, trainer));
        studentService.save(buildValidStudent("Student Light", "2003-01-01", 100.0, trainer));

        List<Student> result = studentService.findByWeightGreaterThan(150.0);

        assertNotNull(result);
        assertEquals(initialSize + 2, result.size());
    }

    @Test
    public void testFindByBirthDate() {
        String uniqueBirthDate = "1988-08-18";
        int initialSize = studentService.findByBirthDate(uniqueBirthDate).size();

        Trainer trainer = trainerService.save(buildValidTrainer("Trainer Birth Student"));
        studentService.save(buildValidStudent("Student Birth A", uniqueBirthDate, 60.0, trainer));
        studentService.save(buildValidStudent("Student Birth B", uniqueBirthDate, 61.0, trainer));
        studentService.save(buildValidStudent("Student Birth C", "1988-08-19", 62.0, trainer));

        List<Student> result = studentService.findByBirthDate(uniqueBirthDate);

        assertNotNull(result);
        assertEquals(initialSize + 2, result.size());
    }
}