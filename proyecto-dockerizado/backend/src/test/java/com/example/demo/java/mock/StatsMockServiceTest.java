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

import com.example.demo.Repository.IStatsRepository;
import com.example.demo.domain.Stats;
import com.example.demo.domain.User;
import com.example.demo.service.impl.StatsService;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class StatsMockServiceTest {

    @Mock
    private IStatsRepository statsRepository;

    @InjectMocks
    private StatsService statsService;

    private User user;
    private Stats stats1;
    private Stats stats2;
    private Stats stats3;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setId(1);
        user.setFullName("Stats User");
        user.setPassword("123");
        user.setBirthDate("2000-01-01");
        user.setWeight(70.0);
        user.setHeight(1.75);

        stats1 = new Stats();
        stats1.setId(1);
        stats1.setUser(user);
        stats1.setTime(2000L);
        stats1.setAmmountExercises(2);

        stats2 = new Stats();
        stats2.setId(2);
        stats2.setUser(user);
        stats2.setTime(4000L);
        stats2.setAmmountExercises(4);

        stats3 = new Stats();
        stats3.setId(3);
        stats3.setUser(user);
        stats3.setTime(6000L);
        stats3.setAmmountExercises(6);
    }

    @Test
    public void testFindAll() {
        when(statsRepository.findAll()).thenReturn(Arrays.asList(stats1, stats2, stats3));

        List<Stats> result = statsService.findAll();

        assertNotNull(result);
        assertEquals(3, result.size());
        verify(statsRepository, times(1)).findAll();
    }

    @Test
    public void testFindById() {
        when(statsRepository.findById(1)).thenReturn(Optional.of(stats1));

        Optional<Stats> result = statsService.findById(1);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().getId());
        verify(statsRepository, times(1)).findById(1);
    }

    @Test
    public void testFindByIdNotFound() {
        when(statsRepository.findById(99)).thenReturn(Optional.empty());

        Optional<Stats> result = statsService.findById(99);

        assertFalse(result.isPresent());
        verify(statsRepository, times(1)).findById(99);
    }

    @Test
    public void testSave() {
        when(statsRepository.save(any(Stats.class))).thenReturn(stats1);

        Stats result = statsService.save(stats1);

        assertNotNull(result);
        assertEquals(1, result.getId());
        verify(statsRepository, times(1)).save(any(Stats.class));
    }

    @Test
    public void testDelete() {
        doNothing().when(statsRepository).deleteById(1);

        statsService.delete(1);

        verify(statsRepository, times(1)).deleteById(1);
    }

    @Test
    public void testFindByTimeGreaterThan() {
        when(statsRepository.findByTimeGreaterThan(3000L)).thenReturn(Arrays.asList(stats2, stats3));

        List<Stats> result = statsService.findByTimeGreaterThan(3000L);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(statsRepository, times(1)).findByTimeGreaterThan(3000L);
    }

    @Test
    public void testFindByAmmountExercises() {
        when(statsRepository.findByAmmountExercises(4)).thenReturn(Arrays.asList(stats2));

        List<Stats> result = statsService.findByAmmountExercises(4);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(4, result.get(0).getAmmountExercises());
        verify(statsRepository, times(1)).findByAmmountExercises(4);
    }

    @Test
    public void testFindByAmmountExercisesGreaterThan() {
        when(statsRepository.findByAmmountExercisesGreaterThan(3)).thenReturn(Arrays.asList(stats2, stats3));

        List<Stats> result = statsService.findByAmmountExercisesGreaterThan(3);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(statsRepository, times(1)).findByAmmountExercisesGreaterThan(3);
    }

    @Test
    public void testFindByUserId() {
        when(statsRepository.findByUser_Id(1)).thenReturn(Arrays.asList(stats1, stats2, stats3));

        List<Stats> result = statsService.findByUserId(1);

        assertNotNull(result);
        assertEquals(3, result.size());
        verify(statsRepository, times(1)).findByUser_Id(1);
    }
}