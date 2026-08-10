package com.example.demo.java.integrations;

import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import com.example.demo.domain.Role;
import com.example.demo.domain.User;
import com.example.demo.service.IUserService;
import com.example.demo.service.RoleService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@SpringBootTest
@ActiveProfiles("demo")
@Transactional
public class RoleIntegrationTest {

    @Autowired
    private RoleService roleService;
    @Autowired
    private IUserService userService;

    private Role buildValidRole(String name) {
        Role role = new Role();
        role.setName(name);
        role.setDescription("Description for " + name);
        return role;
    }

    //  save 

    @Test
    public void testSave() {
        Role saved = roleService.save(buildValidRole("ADMIN"));
        assertNotNull(saved);
        assertNotNull(saved.getId());
        assertEquals("ADMIN", saved.getName());
    }

    //  findAll 

    @Test
    public void testFindAll() {
        roleService.save(buildValidRole("ADMIN"));
        roleService.save(buildValidRole("USER"));

        List<Role> result = roleService.findAll();
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    public void testFindAll_Empty() {
        List<Role> result = roleService.findAll();
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    //  findById 

    @Test
    public void testFindById() {
        Role saved = roleService.save(buildValidRole("MODERATOR"));

        Role result = roleService.findById(saved.getId());
        assertNotNull(result);
        assertEquals(saved.getId(), result.getId());
        assertEquals("MODERATOR", result.getName());
    }

    @Test
    public void testFindById_NotFound() {
        assertThrows(EntityNotFoundException.class, () -> roleService.findById(999L));
    }

    //  update 

    @Test
    public void testUpdate() {
        Role saved = roleService.save(buildValidRole("GUEST"));

        Role changes = new Role();
        changes.setName("SUPER_ADMIN");
        changes.setDescription("Updated description");

        Role updated = roleService.update(saved.getId(), changes);
        assertNotNull(updated);
        assertEquals(saved.getId(), updated.getId());
        assertEquals("SUPER_ADMIN", updated.getName());
        assertEquals("Updated description", updated.getDescription());
    }

    @Test
    public void testUpdate_NotFound() {
        Role changes = buildValidRole("GHOST");
        assertThrows(EntityNotFoundException.class, () -> roleService.update(999L, changes));
    }

    //  deleteById 

    @Test
    public void testDeleteById() {
        Role saved = roleService.save(buildValidRole("TEMP"));
        Long id = saved.getId();

        roleService.deleteById(id);

        assertThrows(EntityNotFoundException.class, () -> roleService.findById(id));
    }

    @Test
    public void testDeleteById_NotFound() {
        assertThrows(EntityNotFoundException.class, () -> roleService.deleteById(999L));
    }

    @Test
    @Transactional
    public void testDeleteById_WithUsersAssigned() {
        // Arrange: role con usuario asignado
        Role role = roleService.save(buildValidRole("DELETABLE"));

        User user = new User();
        user.setFullName("Test User");
        user.setBirthDate("1995-05-15");
        user.setWeight(65.0);
        user.setHeight(1.70);
        user.setPassword("pass");
        user.setRole(role);
        userService.save(user);

        Long roleId = role.getId();

        // Act
        roleService.deleteById(roleId);

        // Assert: el rol fue eliminado
        assertThrows(EntityNotFoundException.class, () -> roleService.findById(roleId));

        // Assert: el usuario existe pero sin rol
        Optional<User> updatedUser = userService.findByFullName("Test User");
        assertTrue(updatedUser.isPresent());
        assertNull(updatedUser.get().getRole());
    }
}