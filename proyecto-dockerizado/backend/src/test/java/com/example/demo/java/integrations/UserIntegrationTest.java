package com.example.demo.java.integrations;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.example.demo.domain.User;
import com.example.demo.service.IUserService;

import jakarta.transaction.Transactional;

@SpringBootTest
@ActiveProfiles("demo")
@Transactional
public class UserIntegrationTest {

    @Autowired
    private IUserService userService;

    private User buildValidUser(String fullName) {
        User user = new User();
        user.setFullName(fullName);
        user.setBirthDate("2000-01-01");
        user.setWeight(70.0);
        user.setHeight(1.75);
        user.setPassword("123");
        return user;
    }

    @Test
    public void testSave() {
        User saved = userService.save(buildValidUser("Juan Perez"));
        assertNotNull(saved);
        assertNotNull(saved.getId());
    }

    @Test
    public void testFindAll() {
        userService.save(buildValidUser("Juan Perez"));
        userService.save(buildValidUser("Maria Lopez"));

        List<User> result = userService.findAll();
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    public void testFindById() {
        User saved = userService.save(buildValidUser("Carlos Gomez"));

        Optional<User> result = userService.findById(saved.getId());
        assertTrue(result.isPresent());
        assertEquals(saved.getId(), result.get().getId());
    }

    @Test
    public void testFindById_NotFound() {
        Optional<User> result = userService.findById(999);
        assertFalse(result.isPresent());
    }

    @Test
    public void testDelete() {
        User saved = userService.save(buildValidUser("Ana Torres"));
        Integer id = saved.getId();

        userService.delete(id);

        Optional<User> result = userService.findById(id);
        assertFalse(result.isPresent());
    }

    @Test
    public void testFindByFullName() {
        userService.save(buildValidUser("Pedro Ramirez"));
        userService.save(buildValidUser("Luis Herrera"));

        Optional<User> result = userService.findByFullName("Pedro Ramirez");
        assertTrue(result.isPresent());
        assertEquals("Pedro Ramirez", result.get().getFullName());
    }

    @Test
    public void testFindByFullName_NotFound() {
        Optional<User> result = userService.findByFullName("No Existe");
        assertFalse(result.isPresent());
    }

    @Test
    public void testFindByFullNameContaining() {
        userService.save(buildValidUser("Juan Perez"));
        userService.save(buildValidUser("Juan Garcia"));
        userService.save(buildValidUser("Maria Lopez"));

        List<User> result = userService.findByFullNameContaining("Juan");
        assertEquals(2, result.size());
    }

    @Test
    public void testFindByFullNameContaining_NotFound() {
        userService.save(buildValidUser("Juan Perez"));

        List<User> result = userService.findByFullNameContaining("Carlos");
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}