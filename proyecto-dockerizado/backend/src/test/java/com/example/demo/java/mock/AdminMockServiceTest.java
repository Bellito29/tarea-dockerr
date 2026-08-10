package com.example.demo.java.mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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

import com.example.demo.Repository.IAdminRepository;
import com.example.demo.domain.Admin;
import com.example.demo.service.impl.AdminService;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class AdminMockServiceTest {

    @Mock
    private IAdminRepository adminRepository;

    @InjectMocks
    private AdminService adminService;

    private Admin admin;
    private Admin savedAdmin;

    @BeforeEach
    public void setUp() {
        admin = new Admin();
        admin.setId(null);
        admin.setFullName("Admin Test");
        admin.setPassword("123");
        admin.setBirthDate("2000-01-01");
        admin.setWeight(70.0);
        admin.setHeight(1.75);

        savedAdmin = new Admin();
        savedAdmin.setId(1);
        savedAdmin.setFullName("Admin Test");
        savedAdmin.setPassword("123");
        savedAdmin.setBirthDate("2000-01-01");
        savedAdmin.setWeight(70.0);
        savedAdmin.setHeight(1.75);
    }

    @Test
    public void testFindAll() {
        when(adminRepository.findAll()).thenReturn(Arrays.asList(savedAdmin));

        List<Admin> result = adminService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(adminRepository, times(1)).findAll();
    }

    @Test
    public void testFindById() {
        when(adminRepository.findById(1)).thenReturn(Optional.of(savedAdmin));

        Optional<Admin> result = adminService.findById(1);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().getId());
        verify(adminRepository, times(1)).findById(1);
    }

    @Test
    public void testFindByIdNotFound() {
        when(adminRepository.findById(1)).thenReturn(Optional.empty());

        Optional<Admin> result = adminService.findById(1);

        assertFalse(result.isPresent());
        verify(adminRepository, times(1)).findById(1);
    }

    @Test
    public void testSave() {
        when(adminRepository.save(any(Admin.class))).thenReturn(savedAdmin);

        Admin result = adminService.save(admin);

        assertNotNull(result);
        assertEquals(1, result.getId());
        verify(adminRepository, times(1)).save(any(Admin.class));
    }

    @Test
    public void testDelete() {
        doNothing().when(adminRepository).deleteById(1);

        adminService.delete(1);

        verify(adminRepository, times(1)).deleteById(1);
    }
}