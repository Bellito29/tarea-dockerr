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

import com.example.demo.domain.Trainer;
import com.example.demo.service.ITrainerService;

import jakarta.transaction.Transactional;

@SpringBootTest
@ActiveProfiles("demo")
@Transactional
public class TrainerServiceIntegrationTest {

    @Autowired
    private ITrainerService trainerService;

    private Trainer buildValidTrainer(String name, String birthDate, Double weight, Double height) {
        Trainer trainer = new Trainer();
        trainer.setFullName(name);
        trainer.setBirthDate(birthDate);
        trainer.setWeight(weight);
        trainer.setHeight(height);
        trainer.setPassword("123");
        return trainer;
    }

    @Test
    public void testSave() {
        Trainer saved = trainerService.save(
            buildValidTrainer("Trainer Save Integration", "1991-01-01", 80.0, 1.80)
        );

        assertNotNull(saved);
        assertNotNull(saved.getId());
    }

    @Test
    public void testFindAll() {
        int initialSize = trainerService.findAll().size();

        trainerService.save(buildValidTrainer("Trainer FindAll A", "1991-01-01", 81.0, 1.81));
        trainerService.save(buildValidTrainer("Trainer FindAll B", "1992-01-01", 82.0, 1.82));

        List<Trainer> result = trainerService.findAll();

        assertNotNull(result);
        assertEquals(initialSize + 2, result.size());
    }

    @Test
    public void testFindById() {
        Trainer saved = trainerService.save(
            buildValidTrainer("Trainer FindById", "1993-01-01", 83.0, 1.83)
        );

        Optional<Trainer> result = trainerService.findById(saved.getId());

        assertTrue(result.isPresent());
        assertEquals(saved.getId(), result.get().getId());
    }

    @Test
    public void testFindById_NotFound() {
        Optional<Trainer> result = trainerService.findById(999999);
        assertFalse(result.isPresent());
    }

    @Test
    public void testDelete() {
        Trainer saved = trainerService.save(
            buildValidTrainer("Trainer Delete", "1994-01-01", 84.0, 1.84)
        );

        Integer id = saved.getId();
        trainerService.delete(id);

        Optional<Trainer> result = trainerService.findById(id);
        assertFalse(result.isPresent());
    }

    @Test
    public void testFindByFullNameContaining() {
        String token = "ITEST_TRAINER_TOKEN";
        int initialSize = trainerService.findByFullNameContaining(token).size();

        trainerService.save(buildValidTrainer(token + "_A", "1990-01-01", 70.0, 1.70));
        trainerService.save(buildValidTrainer(token + "_B", "1991-01-01", 71.0, 1.71));
        trainerService.save(buildValidTrainer("OTHER_TRAINER_NAME", "1992-01-01", 72.0, 1.72));

        List<Trainer> result = trainerService.findByFullNameContaining(token);

        assertNotNull(result);
        assertEquals(initialSize + 2, result.size());
    }

    @Test
    public void testFindByWeightGreaterThan() {
        int initialSize = trainerService.findByWeightGreaterThan(150.0).size();

        trainerService.save(buildValidTrainer("Trainer Heavy A", "1990-01-01", 160.0, 1.80));
        trainerService.save(buildValidTrainer("Trainer Heavy B", "1991-01-01", 170.0, 1.81));
        trainerService.save(buildValidTrainer("Trainer Light", "1992-01-01", 100.0, 1.82));

        List<Trainer> result = trainerService.findByWeightGreaterThan(150.0);

        assertNotNull(result);
        assertEquals(initialSize + 2, result.size());
    }

    @Test
    public void testFindByHeightGreaterThan() {
        int initialSize = trainerService.findByHeightGreaterThan(2.50).size();

        trainerService.save(buildValidTrainer("Trainer Tall A", "1990-01-01", 80.0, 2.60));
        trainerService.save(buildValidTrainer("Trainer Tall B", "1991-01-01", 81.0, 2.70));
        trainerService.save(buildValidTrainer("Trainer Normal", "1992-01-01", 82.0, 2.40));

        List<Trainer> result = trainerService.findByHeightGreaterThan(2.50);

        assertNotNull(result);
        assertEquals(initialSize + 2, result.size());
    }

    @Test
    public void testFindByBirthDate() {
        String uniqueBirthDate = "1987-07-17";
        int initialSize = trainerService.findByBirthDate(uniqueBirthDate).size();

        trainerService.save(buildValidTrainer("Trainer Birth A", uniqueBirthDate, 70.0, 1.70));
        trainerService.save(buildValidTrainer("Trainer Birth B", uniqueBirthDate, 71.0, 1.71));
        trainerService.save(buildValidTrainer("Trainer Birth C", "1987-07-18", 72.0, 1.72));

        List<Trainer> result = trainerService.findByBirthDate(uniqueBirthDate);

        assertNotNull(result);
        assertEquals(initialSize + 2, result.size());
    }
}