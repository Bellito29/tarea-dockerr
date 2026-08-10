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
import com.example.demo.domain.Role;
import jakarta.persistence.EntityNotFoundException;
import com.example.demo.domain.Permission;
import com.example.demo.domain.RolePermission;
import com.example.demo.service.impl.RolePermissionServiceImpl;
import com.example.demo.Repository.RolePermissionRepository;


@ExtendWith(MockitoExtension.class)
public class RolePermissionMockServiceTest {

    @Mock
    private RolePermissionRepository rolePermissionRepository;

    @InjectMocks
    private RolePermissionServiceImpl rolePermissionService;

    private Role role;
    private Permission permission1;
    private Permission permission2;
    private RolePermission rp1;
    private RolePermission rp2;

    @BeforeEach
    public void setUp() {
        role = new Role();
        role.setId(1L);
        role.setName("ADMIN");

        permission1 = new Permission();
        permission1.setId(1);
        permission1.setName("READ");

        permission2 = new Permission();
        permission2.setId(2);
        permission2.setName("WRITE");

        rp1 = new RolePermission();
        rp1.setId(1L);
        rp1.setRole(role);
        rp1.setPermission(permission1);

        rp2 = new RolePermission();
        rp2.setId(2L);
        rp2.setRole(role);
        rp2.setPermission(permission2);
    }

    // findAll 

    @Test
    public void testFindAll() {
        when(rolePermissionRepository.findAll()).thenReturn(Arrays.asList(rp1, rp2));

        List<RolePermission> result = rolePermissionService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(rolePermissionRepository, times(1)).findAll();
    }

    @Test
    public void testFindAll_Empty() {
        when(rolePermissionRepository.findAll()).thenReturn(Arrays.asList());

        List<RolePermission> result = rolePermissionService.findAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(rolePermissionRepository, times(1)).findAll();
    }

    // findById

    @Test
    public void testFindById() {
        when(rolePermissionRepository.findById(1L)).thenReturn(Optional.of(rp1));

        RolePermission result = rolePermissionService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(rolePermissionRepository, times(1)).findById(1L);
    }

    @Test
    public void testFindById_NotFound() {
        when(rolePermissionRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> rolePermissionService.findById(999L));
        verify(rolePermissionRepository, times(1)).findById(999L);
    }

    //  save 

    @Test
    public void testSave() {
        when(rolePermissionRepository.save(rp1)).thenReturn(rp1);

        RolePermission result = rolePermissionService.save(rp1);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("ADMIN", result.getRole().getName());
        assertEquals("READ", result.getPermission().getName());
        verify(rolePermissionRepository, times(1)).save(rp1);
    }

    //  update 

    @Test
    public void testUpdate() {
        RolePermission changes = new RolePermission();
        changes.setRole(role);
        changes.setPermission(permission2);

        when(rolePermissionRepository.findById(1L)).thenReturn(Optional.of(rp1));
        when(rolePermissionRepository.save(any(RolePermission.class))).thenReturn(changes);

        RolePermission result = rolePermissionService.update(1L, changes);

        assertNotNull(result);
        assertEquals("WRITE", result.getPermission().getName());
        verify(rolePermissionRepository, times(1)).findById(1L);
        verify(rolePermissionRepository, times(1)).save(changes);
    }

    @Test
    public void testUpdate_NotFound() {
        when(rolePermissionRepository.findById(999L)).thenReturn(Optional.empty());

        RolePermission changes = new RolePermission();
        changes.setRole(role);
        changes.setPermission(permission1);

        assertThrows(EntityNotFoundException.class, () -> rolePermissionService.update(999L, changes));
        verify(rolePermissionRepository, times(1)).findById(999L);
        verify(rolePermissionRepository, never()).save(any());
    }

    //  deleteById 

    @Test
    public void testDeleteById() {
        when(rolePermissionRepository.findById(1L)).thenReturn(Optional.of(rp1));
        doNothing().when(rolePermissionRepository).delete(rp1);

        rolePermissionService.deleteById(1L);

        verify(rolePermissionRepository, times(1)).findById(1L);
        verify(rolePermissionRepository, times(1)).delete(rp1);
    }

    @Test
    public void testDeleteById_NotFound() {
        when(rolePermissionRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> rolePermissionService.deleteById(999L));
        verify(rolePermissionRepository, times(1)).findById(999L);
        verify(rolePermissionRepository, never()).delete(any());
    }

    //  findByRoleId 

    @Test
    public void testFindByRoleId() {
        when(rolePermissionRepository.findByRoleId(1L)).thenReturn(Arrays.asList(rp1, rp2));

        List<RolePermission> result = rolePermissionService.findByRoleId(1L);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(rolePermissionRepository, times(1)).findByRoleId(1L);
    }

    @Test
    public void testFindByRoleId_Empty() {
        when(rolePermissionRepository.findByRoleId(999L)).thenReturn(Arrays.asList());

        List<RolePermission> result = rolePermissionService.findByRoleId(999L);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(rolePermissionRepository, times(1)).findByRoleId(999L);
    }

    //  findByRoleIdAndPermissionId 

    @Test
    public void testFindByRoleIdAndPermissionId() {
        when(rolePermissionRepository.findByRoleIdAndPermissionId(1L, 1))
                .thenReturn(Optional.of(rp1));

        Optional<RolePermission> result = rolePermissionService
                .findByRoleIdAndPermissionId(1L, 1);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        verify(rolePermissionRepository, times(1)).findByRoleIdAndPermissionId(1L, 1);
    }

    @Test
    public void testFindByRoleIdAndPermissionId_NotFound() {
        when(rolePermissionRepository.findByRoleIdAndPermissionId(999L, 999))
                .thenReturn(Optional.empty());

        Optional<RolePermission> result = rolePermissionService
                .findByRoleIdAndPermissionId(999L, 999);

        assertFalse(result.isPresent());
        verify(rolePermissionRepository, times(1)).findByRoleIdAndPermissionId(999L, 999);
    }

    //  existsByRoleIdAndPermissionId 

    @Test
    public void testExistsByRoleIdAndPermissionId_True() {
        when(rolePermissionRepository.existsByRoleIdAndPermissionId(1L, 1)).thenReturn(true);

        boolean exists = rolePermissionService.existsByRoleIdAndPermissionId(1L, 1);

        assertTrue(exists);
        verify(rolePermissionRepository, times(1)).existsByRoleIdAndPermissionId(1L, 1);
    }

    @Test
    public void testExistsByRoleIdAndPermissionId_False() {
        when(rolePermissionRepository.existsByRoleIdAndPermissionId(999L, 999)).thenReturn(false);

        boolean exists = rolePermissionService.existsByRoleIdAndPermissionId(999L, 999);

        assertFalse(exists);
        verify(rolePermissionRepository, times(1)).existsByRoleIdAndPermissionId(999L, 999);
    }
}