package com.example.demo.java.integrations;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.example.demo.domain.Sesion;
import com.example.demo.domain.User;
import com.example.demo.service.ISesionService;
import com.example.demo.service.IUserService;

import jakarta.transaction.Transactional;

@SpringBootTest
@ActiveProfiles("demo")
@Transactional
public class SesionIntegrationTest {

    @Autowired
    private ISesionService sesionService;

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

    private Sesion buildValidSesion(User user, Long duration) {
        Sesion sesion = new Sesion();
        sesion.setUser(user); 
        sesion.setDuration(duration);
        return sesion;
    }

    @Test
    public void testSave() {
        User user = userService.save(buildValidUser());
        Sesion saved = sesionService.save(buildValidSesion(user, 60L));
        assertNotNull(saved);
        assertNotNull(saved.getId());
    }

    @Test
    public void testFindAll() {
        User user = userService.save(buildValidUser());
        sesionService.save(buildValidSesion(user, 45L));
        sesionService.save(buildValidSesion(user, 30L));

        List<Sesion> result = sesionService.findAll();
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    public void testFindById() {
        User user = userService.save(buildValidUser());
        Sesion saved = sesionService.save(buildValidSesion(user, 90L));

        Optional<Sesion> result = sesionService.findById(saved.getId());
        assertTrue(result.isPresent());
        assertEquals(saved.getId(), result.get().getId());
    }

    @Test
    public void testFindById_NotFound() {
        Optional<Sesion> result = sesionService.findById(999);
        assertFalse(result.isPresent());
    }

    @Test
    public void testDelete() {
        User user = userService.save(buildValidUser());
        Sesion saved = sesionService.save(buildValidSesion(user, 60L));
        Integer id = saved.getId();

        sesionService.delete(id);

        Optional<Sesion> result = sesionService.findById(id);
        assertFalse(result.isPresent());
    }


    @Test
    public void testFindByDurationLessThan() {
        User user = userService.save(buildValidUser());
        sesionService.save(buildValidSesion(user, 30L));
        sesionService.save(buildValidSesion(user, 60L));
        sesionService.save(buildValidSesion(user, 90L));

        List<Sesion> result = sesionService.findByDurationLessThan(60L);
        assertEquals(1, result.size());
        assertTrue(result.get(0).getDuration() < 60L);
    }
}