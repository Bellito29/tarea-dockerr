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

import com.example.demo.Repository.ITrainerRepository;
import com.example.demo.domain.Trainer;
import com.example.demo.service.impl.TrainerService;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class TrainerMockServiceTest {

    @Mock
    private ITrainerRepository trainerRepository;

    @InjectMocks
    private TrainerService trainerService;

    private Trainer trainer1;
    private Trainer trainer2;
    private Trainer trainer3;

    @BeforeEach
    public void setUp() {
        trainer1 = new Trainer();
        trainer1.setId(1);
        trainer1.setFullName("Carlos Diaz");
        trainer1.setPassword("123");
        trainer1.setBirthDate("1990-01-01");
        trainer1.setWeight(70.0);
        trainer1.setHeight(1.70);

        trainer2 = new Trainer();
        trainer2.setId(2);
        trainer2.setFullName("Carlos Perez");
        trainer2.setPassword("123");
        trainer2.setBirthDate("1990-01-01");
        trainer2.setWeight(85.0);
        trainer2.setHeight(1.82);

        trainer3 = new Trainer();
        trainer3.setId(3);
        trainer3.setFullName("Laura Gomez");
        trainer3.setPassword("123");
        trainer3.setBirthDate("1992-01-01");
        trainer3.setWeight(90.0);
        trainer3.setHeight(1.85);
    }

    @Test
    public void testFindAll() {
        when(trainerRepository.findAll()).thenReturn(Arrays.asList(trainer1, trainer2, trainer3));

        List<Trainer> result = trainerService.findAll();

        assertNotNull(result);
        assertEquals(3, result.size());
        verify(trainerRepository, times(1)).findAll();
    }

    @Test
    public void testFindById() {
        when(trainerRepository.findById(1)).thenReturn(Optional.of(trainer1));

        Optional<Trainer> result = trainerService.findById(1);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().getId());
        verify(trainerRepository, times(1)).findById(1);
    }

    @Test
    public void testFindByIdNotFound() {
        when(trainerRepository.findById(99)).thenReturn(Optional.empty());

        Optional<Trainer> result = trainerService.findById(99);

        assertFalse(result.isPresent());
        verify(trainerRepository, times(1)).findById(99);
    }

    @Test
    public void testSave() {
        when(trainerRepository.save(any(Trainer.class))).thenReturn(trainer1);

        Trainer result = trainerService.save(trainer1);

        assertNotNull(result);
        assertEquals(1, result.getId());
        verify(trainerRepository, times(1)).save(any(Trainer.class));
    }

    @Test
    public void testDelete() {
        doNothing().when(trainerRepository).deleteById(1);

        trainerService.delete(1);

        verify(trainerRepository, times(1)).deleteById(1);
    }

    @Test
    public void testFindByFullNameContaining() {
        when(trainerRepository.findByFullNameContaining("Carlos")).thenReturn(Arrays.asList(trainer1, trainer2));

        List<Trainer> result = trainerService.findByFullNameContaining("Carlos");

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(trainerRepository, times(1)).findByFullNameContaining("Carlos");
    }

    @Test
    public void testFindByWeightGreaterThan() {
        when(trainerRepository.findByWeightGreaterThan(80.0)).thenReturn(Arrays.asList(trainer2, trainer3));

        List<Trainer> result = trainerService.findByWeightGreaterThan(80.0);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(trainerRepository, times(1)).findByWeightGreaterThan(80.0);
    }

    @Test
    public void testFindByHeightGreaterThan() {
        when(trainerRepository.findByHeightGreaterThan(1.80)).thenReturn(Arrays.asList(trainer2, trainer3));

        List<Trainer> result = trainerService.findByHeightGreaterThan(1.80);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(trainerRepository, times(1)).findByHeightGreaterThan(1.80);
    }

    @Test
    public void testFindByBirthDate() {
        when(trainerRepository.findByBirthDate("1990-01-01")).thenReturn(Arrays.asList(trainer1, trainer2));

        List<Trainer> result = trainerService.findByBirthDate("1990-01-01");

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(trainerRepository, times(1)).findByBirthDate("1990-01-01");
    }
}