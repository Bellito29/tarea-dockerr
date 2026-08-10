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

import com.example.demo.domain.Admin;
import com.example.demo.service.IAdminService;

import jakarta.transaction.Transactional;

@SpringBootTest
@ActiveProfiles("demo")
@Transactional
public class AdminServiceIntegrationTest {

    @Autowired
    private IAdminService adminService;

    private Admin buildValidAdmin(String name) {
        Admin admin = new Admin();
        admin.setFullName(name);
        admin.setPassword("123");
        admin.setBirthDate("2000-01-01");
        admin.setWeight(70.0);
        admin.setHeight(1.75);
        return admin;
    }

    @Test
    public void testFindAll() {
        adminService.save(buildValidAdmin("Admin Test"));

        List<Admin> admins = adminService.findAll();

        assertNotNull(admins);
        assertEquals(1, admins.size());
    }

    @Test
    public void testFindById() {
        Admin admin = adminService.save(buildValidAdmin("Admin One"));

        Optional<Admin> result = adminService.findById(admin.getId());

        assertTrue(result.isPresent());
        assertEquals(admin.getId(), result.get().getId());
    }

    @Test
    public void testSave() {
        Admin saved = adminService.save(buildValidAdmin("Admin Save"));

        assertNotNull(saved);
        assertNotNull(saved.getId());
    }

    @Test
    public void testDelete() {
        Admin admin = adminService.save(buildValidAdmin("Admin Delete"));

        Integer id = admin.getId();
        adminService.delete(id);

        Optional<Admin> result = adminService.findById(id);
        assertFalse(result.isPresent());
    }
}