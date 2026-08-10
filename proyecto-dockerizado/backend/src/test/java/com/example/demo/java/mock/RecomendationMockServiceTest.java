package com.example.demo.java.mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.demo.Repository.IRecomendationRepository;
import com.example.demo.domain.Recomendation;
import com.example.demo.service.impl.RecomendationService;

@ExtendWith(MockitoExtension.class)
public class RecomendationMockServiceTest {

    @Mock
    private IRecomendationRepository recomendationRepository;

    @InjectMocks
    private RecomendationService recomendationService;

    private Recomendation rec1;
    private Recomendation rec2;
    private Recomendation rec3;

    @BeforeEach
    public void setUp() {
        rec1 = new Recomendation();
        rec1.setId(1);
        rec1.setDescription("Haz cardio todos los días");

        rec2 = new Recomendation();
        rec2.setId(2);
        rec2.setDescription("Haz ejercicio en la mañana");

        rec3 = new Recomendation();
        rec3.setId(3);
        rec3.setDescription("Descansa bien");
    }

    @Test
    public void testSave() {
        when(recomendationRepository.save(rec1)).thenReturn(rec1);

        Recomendation result = recomendationService.save(rec1);

        assertNotNull(result);
        assertEquals("Haz cardio todos los días", result.getDescription());
        verify(recomendationRepository, times(1)).save(rec1);
    }

    @Test
    public void testFindAll() {
        when(recomendationRepository.findAll()).thenReturn(Arrays.asList(rec1, rec2, rec3));

        List<Recomendation> result = recomendationService.findAll();

        assertNotNull(result);
        assertEquals(3, result.size());
        verify(recomendationRepository, times(1)).findAll();
    }

    @Test
    public void testFindById() {
        when(recomendationRepository.findById(1)).thenReturn(Optional.of(rec1));

        Optional<Recomendation> result = recomendationService.findById(1);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().getId());
        verify(recomendationRepository, times(1)).findById(1);
    }

    @Test
    public void testFindById_NotFound() {
        when(recomendationRepository.findById(999)).thenReturn(Optional.empty());

        Optional<Recomendation> result = recomendationService.findById(999);

        assertFalse(result.isPresent());
        verify(recomendationRepository, times(1)).findById(999);
    }

    @Test
    public void testDelete() {
        doNothing().when(recomendationRepository).deleteById(1);

        recomendationService.delete(1);

        verify(recomendationRepository, times(1)).deleteById(1);
    }


    @Test
    public void testFindByDescriptionContaining_Found() {
        when(recomendationRepository.findByDescriptionContaining("cardio"))
            .thenReturn(Arrays.asList(rec1));

        List<Recomendation> result = recomendationService.findByDescriptionContaining("cardio");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get(0).getDescription().contains("cardio"));
        verify(recomendationRepository, times(1)).findByDescriptionContaining("cardio");
    }

    @Test
    public void testFindByDescriptionContaining_MultipleResults() {
        when(recomendationRepository.findByDescriptionContaining("ejercicio"))
            .thenReturn(Arrays.asList(rec2, rec3));

        List<Recomendation> result = recomendationService.findByDescriptionContaining("ejercicio");

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(recomendationRepository, times(1)).findByDescriptionContaining("ejercicio");
    }

    @Test
    public void testFindByDescriptionContaining_NotFound() {
        when(recomendationRepository.findByDescriptionContaining("pesas"))
            .thenReturn(Arrays.asList());

        List<Recomendation> result = recomendationService.findByDescriptionContaining("pesas");

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(recomendationRepository, times(1)).findByDescriptionContaining("pesas");
    }
}