package com.example.demo.java.integrations;

import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import com.example.demo.domain.Role;
import com.example.demo.service.RoleService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import com.example.demo.domain.Permission;
import com.example.demo.domain.RolePermission;
import com.example.demo.service.RolePermissionService;
import com.example.demo.service.IPermissionService;

@SpringBootTest
@ActiveProfiles("demo")
@Transactional
public class RolePermissionIntegrationTest {

    @Autowired
    private RolePermissionService rolePermissionService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private IPermissionService permissionService;

    private Role role;
    private Permission permission1;
    private Permission permission2;

    @BeforeEach
    public void setUp() {
        Role r = new Role();
        r.setName("ADMIN");
        r.setDescription("Admin role");
        role = roleService.save(r);

        Permission p1 = new Permission();
        p1.setName("READ");
        p1.setCode(1022);
        permission1 = permissionService.save(p1);

        Permission p2 = new Permission();
        p2.setName("WRITE");
        p2.setCode(1023);
        permission2 = permissionService.save(p2);
    }

    private RolePermission buildRolePermission(Role role, Permission permission) {
        RolePermission rp = new RolePermission();
        rp.setRole(role);
        rp.setPermission(permission);
        return rp;
    }

    //  save 

    @Test
    public void testSave() {
        RolePermission saved = rolePermissionService.save(buildRolePermission(role, permission1));

        assertNotNull(saved);
        assertNotNull(saved.getId());
        assertEquals(role.getId(), saved.getRole().getId());
        assertEquals(permission1.getId(), saved.getPermission().getId());
    }

    //  findAll 

    @Test
    public void testFindAll() {
        rolePermissionService.save(buildRolePermission(role, permission1));
        rolePermissionService.save(buildRolePermission(role, permission2));

        List<RolePermission> result = rolePermissionService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    public void testFindAll_Empty() {
        List<RolePermission> result = rolePermissionService.findAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    //  findById 

    @Test
    public void testFindById() {
        RolePermission saved = rolePermissionService.save(buildRolePermission(role, permission1));

        RolePermission result = rolePermissionService.findById(saved.getId());

        assertNotNull(result);
        assertEquals(saved.getId(), result.getId());
    }

    @Test
    public void testFindById_NotFound() {
        assertThrows(EntityNotFoundException.class, () -> rolePermissionService.findById(999L));
    }

    //  update 

    @Test
    public void testUpdate() {
        RolePermission saved = rolePermissionService.save(buildRolePermission(role, permission1));

        RolePermission changes = new RolePermission();
        changes.setRole(role);
        changes.setPermission(permission2);

        RolePermission updated = rolePermissionService.update(saved.getId(), changes);

        assertNotNull(updated);
        assertEquals(saved.getId(), updated.getId());
        assertEquals(permission2.getId(), updated.getPermission().getId());
    }

    @Test
    public void testUpdate_NotFound() {
        RolePermission changes = buildRolePermission(role, permission1);
        assertThrows(EntityNotFoundException.class, () -> rolePermissionService.update(999L, changes));
    }

    //  deleteById 

    @Test
    public void testDeleteById() {
        RolePermission saved = rolePermissionService.save(buildRolePermission(role, permission1));
        Long id = saved.getId();

        rolePermissionService.deleteById(id);

        assertThrows(EntityNotFoundException.class, () -> rolePermissionService.findById(id));
    }

    @Test
    public void testDeleteById_NotFound() {
        assertThrows(EntityNotFoundException.class, () -> rolePermissionService.deleteById(999L));
    }

    //  findByRoleId 

    @Test
    public void testFindByRoleId() {
        rolePermissionService.save(buildRolePermission(role, permission1));
        rolePermissionService.save(buildRolePermission(role, permission2));

        List<RolePermission> result = rolePermissionService.findByRoleId(role.getId());

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(rp -> rp.getRole().getId().equals(role.getId())));
    }

    @Test
    public void testFindByRoleId_NotFound() {
        List<RolePermission> result = rolePermissionService.findByRoleId(999L);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    //  findByRoleIdAndPermissionId 

    @Test
    public void testFindByRoleIdAndPermissionId() {
        rolePermissionService.save(buildRolePermission(role, permission1));

        Optional<RolePermission> result = rolePermissionService
                .findByRoleIdAndPermissionId(role.getId(), permission1.getId());

        assertTrue(result.isPresent());
        assertEquals(role.getId(), result.get().getRole().getId());
        assertEquals(permission1.getId(), result.get().getPermission().getId());
    }

    @Test
    public void testFindByRoleIdAndPermissionId_NotFound() {
        Optional<RolePermission> result = rolePermissionService
                .findByRoleIdAndPermissionId(999L, 999);

        assertFalse(result.isPresent());
    }

    //  existsByRoleIdAndPermissionId  ────────────────────────────────────────────────

    @Test
    public void testExistsByRoleIdAndPermissionId_True() {
        rolePermissionService.save(buildRolePermission(role, permission1));

        boolean exists = rolePermissionService
                .existsByRoleIdAndPermissionId(role.getId(), permission1.getId());

        assertTrue(exists);
    }

    @Test
    public void testExistsByRoleIdAndPermissionId_False() {
        boolean exists = rolePermissionService
                .existsByRoleIdAndPermissionId(999L, 999);

        assertFalse(exists);
    }
}