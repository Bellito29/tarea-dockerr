package com.example.demo.java.integrations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.example.demo.domain.Exercise;
import com.example.demo.service.IExerciceService;

import jakarta.transaction.Transactional;


@SpringBootTest
@ActiveProfiles("demo")
@Transactional
public class ExerciseServiceIntegrationTest {

    @Autowired
    private IExerciceService exerciceService;

    private Exercise buildValidExercise() {
        Exercise ex = new Exercise();
        ex.setName("Push Up");
        ex.setType("Strength");
        ex.setDescription("Test exercise");
        ex.setDuration(10);
        ex.setDifficulty("Medium");
        ex.setVideoUrl("http://test.com");

        return ex;
    }

    @Test
    public void testFindAll() {
        exerciceService.save(buildValidExercise());

        List<Exercise> list = exerciceService.findAll();

        assertNotNull(list);
        assertEquals(1, list.size());
    }

    @Test
    public void testFindById() {
        Exercise ex = exerciceService.save(buildValidExercise());

        Optional<Exercise> result = exerciceService.findById(ex.getId());

        assertTrue(result.isPresent());
        assertEquals(ex.getId(), result.get().getId());
    }

    @Test
    public void testSave() {
        Exercise saved = exerciceService.save(buildValidExercise());

        assertNotNull(saved);
        assertNotNull(saved.getId());
    }

    @Test
    public void testDelete() {
        Exercise ex = exerciceService.save(buildValidExercise());

        exerciceService.delete(ex.getId());

        Optional<Exercise> result = exerciceService.findById(ex.getId());
        assertFalse(result.isPresent());
    }
}