package com.example.demo.java.mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import com.example.demo.domain.Permission;
import com.example.demo.service.impl.PermissionService;
import com.example.demo.Repository.IPermissionRepository;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class PermissionServiceTest {

    @Mock
    private IPermissionRepository permissionRepository;

    @InjectMocks
    private PermissionService permissionService;

    private Permission permission;
    private Permission savedPermission;

    @BeforeEach
    public void setUp() {

        permission = new Permission();
        permission.setId(null);
        permission.setCode(100);

        savedPermission = new Permission();
        savedPermission.setId(1);
        savedPermission.setCode(100);
    }

    @Test
    public void testFindAll() {
        when(permissionRepository.findAll())
                .thenReturn(Arrays.asList(savedPermission));

        List<Permission> permissions = permissionService.findAll();

        assertNotNull(permissions);
        assertEquals(1, permissions.size());
        verify(permissionRepository, times(1)).findAll();
    }

    @Test
    public void testFindById() {
        when(permissionRepository.findById(1))
                .thenReturn(Optional.of(savedPermission));

        Permission result = permissionService.findById(1)
                .orElseThrow(() -> new RuntimeException("Permission not found"));

        assertNotNull(result);
        assertEquals(1, result.getId());
        verify(permissionRepository, times(1)).findById(1);
    }

    @Test
    public void testFindByIdNotFound() {
        when(permissionRepository.findById(1))
                .thenReturn(Optional.empty());

        Optional<Permission> result = permissionService.findById(1);

        assertTrue(result.isEmpty());
    }

    @Test
    public void testSave() {
        when(permissionRepository.save(any(Permission.class)))
                .thenReturn(savedPermission);

        Permission result = permissionService.save(permission);

        assertNotNull(result);
        assertEquals(100, result.getCode());
        verify(permissionRepository, times(1)).save(any(Permission.class));
    }

    @Test
    public void testDeleteById() {
        doNothing().when(permissionRepository).deleteById(1);
        when(permissionRepository.findById(1))
                .thenReturn(Optional.empty());

        permissionService.deleteById(1);

        Optional<Permission> result = permissionService.findById(1);

        assertTrue(result.isEmpty());
    }
}
