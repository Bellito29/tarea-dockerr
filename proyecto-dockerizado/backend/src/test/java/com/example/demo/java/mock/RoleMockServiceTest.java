package com.example.demo.java.mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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
import com.example.demo.Repository.IUserRepository;
import com.example.demo.Repository.RoleRepository;
import com.example.demo.domain.Role;
import com.example.demo.service.impl.RoleServiceImpl;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext; 


@ExtendWith(MockitoExtension.class)
public class RoleMockServiceTest {

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private IUserRepository userRepository;

    @Mock
    @PersistenceContext
    private EntityManager entityManager;

    @InjectMocks
    private RoleServiceImpl roleService;


    private Role role1;
    private Role role2;

    @BeforeEach
    public void setUp() {
        role1 = new Role();
        role1.setId(1L);
        role1.setName("ADMIN");
        role1.setDescription("Administrator role");

        role2 = new Role();
        role2.setId(2L);
        role2.setName("USER");
        role2.setDescription("Regular user role");
    }

    //  findAll 

    @Test
    public void testFindAll() {
        when(roleRepository.findAll()).thenReturn(Arrays.asList(role1, role2));

        List<Role> result = roleService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(roleRepository, times(1)).findAll();
    }

    @Test
    public void testFindAll_Empty() {
        when(roleRepository.findAll()).thenReturn(Arrays.asList());

        List<Role> result = roleService.findAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(roleRepository, times(1)).findAll();
    }

    //  findById 

    @Test
    public void testFindById() {
        when(roleRepository.findById(1L)).thenReturn(Optional.of(role1));

        Role result = roleService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("ADMIN", result.getName());
        verify(roleRepository, times(1)).findById(1L);
    }

    @Test
    public void testFindById_NotFound() {
        when(roleRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> roleService.findById(999L));
        verify(roleRepository, times(1)).findById(999L);
    }

    //  save

    @Test
    public void testSave() {
        when(roleRepository.save(role1)).thenReturn(role1);

        Role result = roleService.save(role1);

        assertNotNull(result);
        assertEquals("ADMIN", result.getName());
        verify(roleRepository, times(1)).save(role1);
    }

    //  update 

    @Test
    public void testUpdate() {
        Role changes = new Role();
        changes.setName("SUPER_ADMIN");
        changes.setDescription("Updated description");

        when(roleRepository.findById(1L)).thenReturn(Optional.of(role1));
        when(roleRepository.save(any(Role.class))).thenReturn(changes);

        Role result = roleService.update(1L, changes);

        assertNotNull(result);
        assertEquals("SUPER_ADMIN", result.getName());
        verify(roleRepository, times(1)).findById(1L);
        verify(roleRepository, times(1)).save(changes);
    }

    @Test
    public void testUpdate_NotFound() {
        when(roleRepository.findById(999L)).thenReturn(Optional.empty());

        Role changes = new Role();
        changes.setName("GHOST");

        assertThrows(EntityNotFoundException.class, () -> roleService.update(999L, changes));
        verify(roleRepository, times(1)).findById(999L);
        verify(roleRepository, never()).save(any());
    }

    //  deleteById 

    @Test
    public void testDeleteById() {
        when(roleRepository.findById(1L)).thenReturn(Optional.of(role1));
        when(userRepository.findByRoleId(1L)).thenReturn(Arrays.asList());
        doNothing().when(roleRepository).deleteById(1L);

        roleService.deleteById(1L);

        verify(roleRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).findByRoleId(1L);
        verify(roleRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteById_NotFound() {
        when(roleRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> roleService.deleteById(999L));
        verify(roleRepository, times(1)).findById(999L);
        verify(roleRepository, never()).deleteById(any());
    }

}