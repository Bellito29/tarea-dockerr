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

import com.example.demo.domain.Permission;
import com.example.demo.service.impl.PermissionService;

import jakarta.transaction.Transactional;

@SpringBootTest
@ActiveProfiles("demo")
@Transactional
public class PermissionServiceIntegrationTest {

    @Autowired
    private PermissionService permissionService;

    private Permission buildPermission(Integer code) {
        Permission p = new Permission();
        p.setCode(code);
        return p;
    }

    @Test
    public void testFindAll() {
        permissionService.save(buildPermission(100));

        List<Permission> permissions = permissionService.findAll();

        assertNotNull(permissions);
        assertEquals(1, permissions.size());
    }

    @Test
    public void testFindById() {
        Permission saved = permissionService.save(buildPermission(200));

        Optional<Permission> result = permissionService.findById(saved.getId());

        assertTrue(result.isPresent());
        assertEquals(saved.getId(), result.get().getId());
    }

    @Test
    public void testFindByIdNotFound() {
        Optional<Permission> result = permissionService.findById(999);

        assertTrue(result.isEmpty());
    }

    @Test
    public void testSave() {
        Permission saved = permissionService.save(buildPermission(300));

        assertNotNull(saved);
        assertNotNull(saved.getId());
        assertEquals(300, saved.getCode());
    }

    @Test
    public void testDeleteById() {
        Permission saved = permissionService.save(buildPermission(400));

        Integer id = saved.getId();
        permissionService.deleteById(id);

        Optional<Permission> result = permissionService.findById(id);

        assertFalse(result.isPresent());
    }
}
