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

import com.example.demo.domain.Recomendation;
import com.example.demo.service.IRecomendationService;

import jakarta.transaction.Transactional;

@SpringBootTest
@ActiveProfiles("demo")
@Transactional
public class RecomendationIntegrationTest {

    @Autowired
    private IRecomendationService recomendationService;

    private Recomendation buildValidRecomendation(String description) {
        Recomendation rec = new Recomendation();
        rec.setDescription(description);
        return rec;
    }


    @Test
    public void testSave() {
        Recomendation saved = recomendationService.save(buildValidRecomendation("Bebe agua"));
        assertNotNull(saved);
        assertNotNull(saved.getId());
    }

    @Test
    public void testFindAll() {
        recomendationService.save(buildValidRecomendation("Descansa bien"));
        recomendationService.save(buildValidRecomendation("Come proteína"));

        List<Recomendation> result = recomendationService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    public void testFindById() {
        Recomendation saved = recomendationService.save(buildValidRecomendation("Estira antes"));

        Optional<Recomendation> result = recomendationService.findById(saved.getId());

        assertTrue(result.isPresent());
        assertEquals(saved.getId(), result.get().getId());
    }

    @Test
    public void testFindById_NotFound() {
        Optional<Recomendation> result = recomendationService.findById(999);
        assertFalse(result.isPresent());
    }

    @Test
    public void testDelete() {
        Recomendation saved = recomendationService.save(buildValidRecomendation("Hidratate"));
        Integer id = saved.getId();

        recomendationService.delete(id);

        Optional<Recomendation> result = recomendationService.findById(id);
        assertFalse(result.isPresent());
    }


    @Test
    public void testFindByDescriptionContaining_Found() {
        recomendationService.save(buildValidRecomendation("Haz cardio todos los días"));
        recomendationService.save(buildValidRecomendation("Descansa los fines de semana"));
        recomendationService.save(buildValidRecomendation("Come frutas y verduras"));

        List<Recomendation> result = recomendationService.findByDescriptionContaining("cardio");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get(0).getDescription().contains("cardio"));
    }

    @Test
    public void testFindByDescriptionContaining_MultipleResults() {
        recomendationService.save(buildValidRecomendation("Haz ejercicio en la mañana"));
        recomendationService.save(buildValidRecomendation("Haz ejercicio en la tarde"));
        recomendationService.save(buildValidRecomendation("Descansa bien"));

        List<Recomendation> result = recomendationService.findByDescriptionContaining("ejercicio");

        assertEquals(2, result.size());
    }

    @Test
    public void testFindByDescriptionContaining_NotFound() {
        recomendationService.save(buildValidRecomendation("Bebe agua"));

        List<Recomendation> result = recomendationService.findByDescriptionContaining("pesas");

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}