package com.example.demo.java.mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import com.example.demo.Repository.IExerciceRepository;
import com.example.demo.domain.Exercise;
import com.example.demo.service.impl.ExerciceService;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class ExerciseMockServiceTest {

    @Mock
    private IExerciceRepository exerciceRepository;

    @InjectMocks
    private ExerciceService exerciceService;

    private Exercise exercise;
    private Exercise savedExercise;

    @BeforeEach
    public void setUp() {
        exercise = new Exercise();
        exercise.setId(null);
        exercise.setName("Push Up");
        exercise.setType("Strength");
        exercise.setDescription("Test");
        exercise.setDuration(10);
        exercise.setDifficulty("Medium");
        exercise.setVideoUrl("url");

        savedExercise = new Exercise();
        savedExercise.setId(5);
        savedExercise.setName("Push Up");
        savedExercise.setType("Strength");
        savedExercise.setDescription("Test");
        savedExercise.setDuration(10);
        savedExercise.setDifficulty("Medium");
        savedExercise.setVideoUrl("url");
    }

    @Test
    public void testFindAll() {
        when(exerciceRepository.findAll()).thenReturn(Arrays.asList(savedExercise));

        List<Exercise> result = exerciceService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(exerciceRepository, times(1)).findAll();
    }

    @Test
    public void testFindById() {
        when(exerciceRepository.findById(5)).thenReturn(Optional.of(savedExercise));

        Optional<Exercise> result = exerciceService.findById(5);

        assertTrue(result.isPresent());
        assertEquals(5, result.get().getId());
        verify(exerciceRepository, times(1)).findById(5);
    }

    @Test
    public void testFindByIdNotFound() {
        when(exerciceRepository.findById(5)).thenReturn(Optional.empty());

        Optional<Exercise> result = exerciceService.findById(5);

        assertFalse(result.isPresent());
        verify(exerciceRepository, times(1)).findById(5);
    }

    @Test
    public void testSave() {
        when(exerciceRepository.save(any(Exercise.class))).thenReturn(savedExercise);

        Exercise result = exerciceService.save(exercise);

        assertNotNull(result);
        assertEquals(5, result.getId());
        verify(exerciceRepository, times(1)).save(any(Exercise.class));
    }

    @Test
    public void testDelete() {
        doNothing().when(exerciceRepository).deleteById(5);

        exerciceService.delete(5);

        verify(exerciceRepository, times(1)).deleteById(5);
    }
}