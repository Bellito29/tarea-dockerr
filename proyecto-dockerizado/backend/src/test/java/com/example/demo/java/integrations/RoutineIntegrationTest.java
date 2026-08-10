package com.example.demo.java.integrations;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.example.demo.domain.Routine;
import com.example.demo.domain.User;
import com.example.demo.service.IRoutineService;
import com.example.demo.service.IUserService;

import jakarta.transaction.Transactional;

@SpringBootTest
@ActiveProfiles("demo")
@Transactional
public class RoutineIntegrationTest {

    @Autowired
    private IRoutineService routineService;

    @Autowired
    private IUserService userService;

    private User buildValidUser() {
        User user = new User();
        user.setFullName("Test User");
        user.setBirthDate("2000-01-01");
        user.setWeight(70.0);
        user.setHeight(1.75);
        user.setPassword("123");
        return user;
    }

    private Routine buildValidRoutine(User user, String name, String description, String visibility) {
        Routine routine = new Routine();
        routine.setUser(user);
        routine.setRoutineName(name);
        routine.setDescription(description);
        routine.setVisibility(visibility);
        return routine;
    }

    @Test
    public void testSave() {
        User user = userService.save(buildValidUser());
        Routine saved = routineService.save(buildValidRoutine(user, "Rutina A", "Desc A", "public"));
        assertNotNull(saved);
        assertNotNull(saved.getId());
    }

    @Test
    public void testFindAll() {
        User user = userService.save(buildValidUser());
        routineService.save(buildValidRoutine(user, "Rutina A", "Desc A", "public"));
        routineService.save(buildValidRoutine(user, "Rutina B", "Desc B", "private"));

        List<Routine> result = routineService.findAll();
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    public void testFindById() {
        User user = userService.save(buildValidUser());
        Routine saved = routineService.save(buildValidRoutine(user, "Rutina A", "Desc A", "public"));

        Optional<Routine> result = routineService.findById(saved.getId());
        assertTrue(result.isPresent());
        assertEquals(saved.getId(), result.get().getId());
    }

    @Test
    public void testFindById_NotFound() {
        Optional<Routine> result = routineService.findById(999);
        assertFalse(result.isPresent());
    }

    @Test
    public void testDelete() {
        User user = userService.save(buildValidUser());
        Routine saved = routineService.save(buildValidRoutine(user, "Rutina A", "Desc A", "public"));
        Integer id = saved.getId();

        routineService.delete(id);

        Optional<Routine> result = routineService.findById(id);
        assertFalse(result.isPresent());
    }

    @Test
    public void testFindByRoutineName() {
        User user = userService.save(buildValidUser());
        routineService.save(buildValidRoutine(user, "Cardio", "Desc", "public"));
        routineService.save(buildValidRoutine(user, "Fuerza", "Desc", "public"));

        List<Routine> result = routineService.findByRoutineName("Cardio");
        assertEquals(1, result.size());
        assertEquals("Cardio", result.get(0).getRoutineName());
    }

    @Test
    public void testFindByRoutineNameContaining() {
        User user = userService.save(buildValidUser());
        routineService.save(buildValidRoutine(user, "Cardio Intenso", "Desc", "public"));
        routineService.save(buildValidRoutine(user, "Cardio Suave", "Desc", "public"));
        routineService.save(buildValidRoutine(user, "Fuerza", "Desc", "public"));

        List<Routine> result = routineService.findByRoutineNameContaining("Cardio");
        assertEquals(2, result.size());
    }


    @Test
    public void testFindByDescriptionContaining() {
        User user = userService.save(buildValidUser());
        routineService.save(buildValidRoutine(user, "Rutina A", "Ejercicios de pierna", "public"));
        routineService.save(buildValidRoutine(user, "Rutina B", "Ejercicios de brazo", "public"));
        routineService.save(buildValidRoutine(user, "Rutina C", "Descanso activo", "public"));

        List<Routine> result = routineService.findByDescriptionContaining("Ejercicios");
        assertEquals(2, result.size());
    }

    @Test
    public void testFindByDescriptionContaining_NotFound() {
        User user = userService.save(buildValidUser());
        routineService.save(buildValidRoutine(user, "Rutina A", "Cardio", "public"));

        List<Routine> result = routineService.findByDescriptionContaining("Yoga");
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}