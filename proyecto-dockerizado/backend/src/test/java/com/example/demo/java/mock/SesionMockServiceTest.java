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

import com.example.demo.Repository.ISesionRepository;
import com.example.demo.domain.Sesion;
import com.example.demo.domain.User;
import com.example.demo.service.impl.SesionService;

@ExtendWith(MockitoExtension.class)
public class SesionMockServiceTest {

    @Mock
    private ISesionRepository sesionRepository;

    @InjectMocks
    private SesionService sesionService;

    private User user;
    private Sesion sesion1;
    private Sesion sesion2;
    private Sesion sesion3;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setId(1);
        user.setFullName("Test User");
        user.setBirthDate("2000-01-01");
        user.setWeight(70.0);
        user.setHeight(1.75);
        user.setPassword("123");

        sesion1 = new Sesion();
        sesion1.setId(1);
        sesion1.setUser(user);
        sesion1.setDuration(30L);

        sesion2 = new Sesion();
        sesion2.setId(2);
        sesion2.setUser(user);
        sesion2.setDuration(60L);

        sesion3 = new Sesion();
        sesion3.setId(3);
        sesion3.setUser(user);
        sesion3.setDuration(90L);
    }


    @Test
    public void testSave() {
        when(sesionRepository.save(sesion1)).thenReturn(sesion1);

        Sesion result = sesionService.save(sesion1);

        assertNotNull(result);
        assertEquals(30L, result.getDuration());
        verify(sesionRepository, times(1)).save(sesion1);
    }

    @Test
    public void testFindAll() {
        when(sesionRepository.findAll()).thenReturn(Arrays.asList(sesion1, sesion2, sesion3));

        List<Sesion> result = sesionService.findAll();

        assertNotNull(result);
        assertEquals(3, result.size());
        verify(sesionRepository, times(1)).findAll();
    }

    @Test
    public void testFindById() {
        when(sesionRepository.findById(1)).thenReturn(Optional.of(sesion1));

        Optional<Sesion> result = sesionService.findById(1);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().getId());
        verify(sesionRepository, times(1)).findById(1);
    }

    @Test
    public void testFindById_NotFound() {
        when(sesionRepository.findById(999)).thenReturn(Optional.empty());

        Optional<Sesion> result = sesionService.findById(999);

        assertFalse(result.isPresent());
        verify(sesionRepository, times(1)).findById(999);
    }

    @Test
    public void testDelete() {
        doNothing().when(sesionRepository).deleteById(1);

        sesionService.delete(1);

        verify(sesionRepository, times(1)).deleteById(1);
    }


    @Test
    public void testFindByDurationLessThan() {
        when(sesionRepository.findByDurationLessThan(60L))
            .thenReturn(Arrays.asList(sesion1)); // solo duration=30 es menor que 60

        List<Sesion> result = sesionService.findByDurationLessThan(60L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get(0).getDuration() < 60L);
        verify(sesionRepository, times(1)).findByDurationLessThan(60L);
    }

    @Test
    public void testFindByDurationLessThan_NotFound() {
        when(sesionRepository.findByDurationLessThan(10L))
            .thenReturn(Arrays.asList());

        List<Sesion> result = sesionService.findByDurationLessThan(10L);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(sesionRepository, times(1)).findByDurationLessThan(10L);
    }
}