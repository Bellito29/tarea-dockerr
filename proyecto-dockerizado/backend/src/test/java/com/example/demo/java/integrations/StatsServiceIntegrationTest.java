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

import com.example.demo.domain.Stats;
import com.example.demo.domain.User;
import com.example.demo.service.IStatsService;
import com.example.demo.service.IUserService;

import jakarta.transaction.Transactional;

@SpringBootTest
@ActiveProfiles("demo")
@Transactional
public class StatsServiceIntegrationTest {

    @Autowired
    private IStatsService statsService;

    @Autowired
    private IUserService userService;

    private User buildValidUser(String name) {
        User user = new User();
        user.setFullName(name);
        user.setBirthDate("2000-01-01");
        user.setWeight(70.0);
        user.setHeight(1.75);
        user.setPassword("123");
        return user;
    }

    private Stats buildValidStats(User user, Long time, Integer ammountExercises) {
        Stats stats = new Stats();
        stats.setUser(user);
        stats.setTime(time);
        stats.setAmmountExercises(ammountExercises);
        return stats;
    }

    @Test
    public void testSave() {
        User user = userService.save(buildValidUser("Stats User Save"));
        Stats saved = statsService.save(buildValidStats(user, 5000L, 10));

        assertNotNull(saved);
        assertNotNull(saved.getId());
    }

    @Test
    public void testFindAll() {
        int initialSize = statsService.findAll().size();

        User user = userService.save(buildValidUser("Stats User FindAll"));
        statsService.save(buildValidStats(user, 6000L, 11));
        statsService.save(buildValidStats(user, 7000L, 12));

        List<Stats> result = statsService.findAll();

        assertNotNull(result);
        assertEquals(initialSize + 2, result.size());
    }

    @Test
    public void testFindById() {
        User user = userService.save(buildValidUser("Stats User FindById"));
        Stats saved = statsService.save(buildValidStats(user, 8000L, 13));

        Optional<Stats> result = statsService.findById(saved.getId());

        assertTrue(result.isPresent());
        assertEquals(saved.getId(), result.get().getId());
    }

    @Test
    public void testFindById_NotFound() {
        Optional<Stats> result = statsService.findById(999999);
        assertFalse(result.isPresent());
    }

    @Test
    public void testDelete() {
        User user = userService.save(buildValidUser("Stats User Delete"));
        Stats saved = statsService.save(buildValidStats(user, 9000L, 14));
        Integer id = saved.getId();

        statsService.delete(id);

        Optional<Stats> result = statsService.findById(id);
        assertFalse(result.isPresent());
    }

    @Test
    public void testFindByTimeGreaterThan() {
        int initialSize = statsService.findByTimeGreaterThan(900000L).size();

        User user = userService.save(buildValidUser("Stats User Time"));
        statsService.save(buildValidStats(user, 1000000L, 20));
        statsService.save(buildValidStats(user, 1000001L, 21));
        statsService.save(buildValidStats(user, 100L, 22));

        List<Stats> result = statsService.findByTimeGreaterThan(900000L);

        assertNotNull(result);
        assertEquals(initialSize + 2, result.size());
    }

    @Test
    public void testFindByAmmountExercises() {
        int initialSize = statsService.findByAmmountExercises(777).size();

        User user = userService.save(buildValidUser("Stats User Amount"));
        statsService.save(buildValidStats(user, 11000L, 777));
        statsService.save(buildValidStats(user, 12000L, 777));
        statsService.save(buildValidStats(user, 13000L, 778));

        List<Stats> result = statsService.findByAmmountExercises(777);

        assertNotNull(result);
        assertEquals(initialSize + 2, result.size());
    }

    @Test
    public void testFindByAmmountExercisesGreaterThan() {
        int initialSize = statsService.findByAmmountExercisesGreaterThan(900).size();

        User user = userService.save(buildValidUser("Stats User Amount Greater"));
        statsService.save(buildValidStats(user, 14000L, 901));
        statsService.save(buildValidStats(user, 15000L, 950));
        statsService.save(buildValidStats(user, 16000L, 100));

        List<Stats> result = statsService.findByAmmountExercisesGreaterThan(900);

        assertNotNull(result);
        assertEquals(initialSize + 2, result.size());
    }

    @Test
    public void testFindByUserId() {
        User user = userService.save(buildValidUser("Stats User ByUserId"));

        statsService.save(buildValidStats(user, 17000L, 30));
        statsService.save(buildValidStats(user, 18000L, 31));

        List<Stats> result = statsService.findByUserId(user.getId());

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(s -> s.getUser().getId().equals(user.getId())));
    }
}