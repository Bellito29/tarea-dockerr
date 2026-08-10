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

import com.example.demo.Repository.IRoutineRepository;
import com.example.demo.domain.Routine;
import com.example.demo.domain.User;
import com.example.demo.service.impl.RoutineService;

@ExtendWith(MockitoExtension.class)
public class RoutineMockServiceTest {

    @Mock
    private IRoutineRepository routineRepository;

    @InjectMocks
    private RoutineService routineService;

    private User user;
    private Routine routine1;
    private Routine routine2;
    private Routine routine3;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setId(1);
        user.setFullName("Test User");
        user.setBirthDate("2000-01-01");
        user.setWeight(70.0);
        user.setHeight(1.75);
        user.setPassword("123");

        routine1 = new Routine();
        routine1.setId(1);
        routine1.setUser(user);
        routine1.setRoutineName("Cardio Intenso");
        routine1.setDescription("Ejercicios de pierna");
        routine1.setVisibility("public");

        routine2 = new Routine();
        routine2.setId(2);
        routine2.setUser(user);
        routine2.setRoutineName("Cardio Suave");
        routine2.setDescription("Ejercicios de brazo");
        routine2.setVisibility("public");

        routine3 = new Routine();
        routine3.setId(3);
        routine3.setUser(user);
        routine3.setRoutineName("Fuerza");
        routine3.setDescription("Descanso activo");
        routine3.setVisibility("private");
    }

    @Test
    public void testSave() {
        when(routineRepository.save(routine1)).thenReturn(routine1);

        Routine result = routineService.save(routine1);

        assertNotNull(result);
        assertEquals("Cardio Intenso", result.getRoutineName());
        verify(routineRepository, times(1)).save(routine1);
    }

    @Test
    public void testFindAll() {
        when(routineRepository.findAll()).thenReturn(Arrays.asList(routine1, routine2, routine3));

        List<Routine> result = routineService.findAll();

        assertNotNull(result);
        assertEquals(3, result.size());
        verify(routineRepository, times(1)).findAll();
    }

    @Test
    public void testFindById() {
        when(routineRepository.findById(1)).thenReturn(Optional.of(routine1));

        Optional<Routine> result = routineService.findById(1);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().getId());
        verify(routineRepository, times(1)).findById(1);
    }

    @Test
    public void testFindById_NotFound() {
        when(routineRepository.findById(999)).thenReturn(Optional.empty());

        Optional<Routine> result = routineService.findById(999);

        assertFalse(result.isPresent());
        verify(routineRepository, times(1)).findById(999);
    }

    @Test
    public void testDelete() {
        doNothing().when(routineRepository).deleteById(1);

        routineService.delete(1);

        verify(routineRepository, times(1)).deleteById(1);
    }

    @Test
    public void testFindByRoutineName() {
        when(routineRepository.findByRoutineName("Cardio Intenso"))
            .thenReturn(Arrays.asList(routine1));

        List<Routine> result = routineService.findByRoutineName("Cardio Intenso");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Cardio Intenso", result.get(0).getRoutineName());
        verify(routineRepository, times(1)).findByRoutineName("Cardio Intenso");
    }

    @Test
    public void testFindByRoutineName_NotFound() {
        when(routineRepository.findByRoutineName("No Existe"))
            .thenReturn(Arrays.asList());

        List<Routine> result = routineService.findByRoutineName("No Existe");

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(routineRepository, times(1)).findByRoutineName("No Existe");
    }

    @Test
    public void testFindByRoutineNameContaining() {
        when(routineRepository.findByRoutineNameContaining("Cardio"))
            .thenReturn(Arrays.asList(routine1, routine2));

        List<Routine> result = routineService.findByRoutineNameContaining("Cardio");

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(routineRepository, times(1)).findByRoutineNameContaining("Cardio");
    }

    @Test
    public void testFindByRoutineNameContaining_NotFound() {
        when(routineRepository.findByRoutineNameContaining("Yoga"))
            .thenReturn(Arrays.asList());

        List<Routine> result = routineService.findByRoutineNameContaining("Yoga");

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(routineRepository, times(1)).findByRoutineNameContaining("Yoga");
    }

    @Test
    public void testFindByDescriptionContaining() {
        when(routineRepository.findByDescriptionContaining("Ejercicios"))
            .thenReturn(Arrays.asList(routine1, routine2));

        List<Routine> result = routineService.findByDescriptionContaining("Ejercicios");

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(routineRepository, times(1)).findByDescriptionContaining("Ejercicios");
    }

    @Test
    public void testFindByDescriptionContaining_NotFound() {
        when(routineRepository.findByDescriptionContaining("Yoga"))
            .thenReturn(Arrays.asList());

        List<Routine> result = routineService.findByDescriptionContaining("Yoga");

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(routineRepository, times(1)).findByDescriptionContaining("Yoga");
    }
}