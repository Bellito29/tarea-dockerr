package com.example.demo.java.mock;

import static org.junit.jupiter.api.Assertions.*;
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
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.demo.Repository.IUserRepository;
import com.example.demo.domain.User;
import com.example.demo.service.impl.UserService;

@ExtendWith(MockitoExtension.class)
public class UserMockServiceTest {

    @Mock
    private IUserRepository userRepository;

    @InjectMocks
    private UserService userService; 

    @Mock
    private PasswordEncoder passwordEncoder;

    private User user1;
    private User user2;

    @BeforeEach
    public void setUp() {
        user1 = new User();
        user1.setId(1);
        user1.setFullName("Juan Perez");
        user1.setBirthDate("2000-01-01");
        user1.setWeight(70.0);
        user1.setHeight(1.75);
        user1.setPassword("123");

        user2 = new User();
        user2.setId(2);
        user2.setFullName("Maria Lopez");
        user2.setBirthDate("1995-05-15");
        user2.setWeight(60.0);
        user2.setHeight(1.65);
        user2.setPassword("456");
    }

    @Test
    public void testSave() {
        when(userRepository.save(user1)).thenReturn(user1);

        User result = userService.save(user1);

        assertNotNull(result);
        assertEquals("Juan Perez", result.getFullName());
        verify(userRepository, times(1)).save(user1);
    }

    @Test
    public void testFindAll() {
        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        List<User> result = userService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    public void testFindById() {
        when(userRepository.findById(1)).thenReturn(Optional.of(user1));

        Optional<User> result = userService.findById(1);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().getId());
        verify(userRepository, times(1)).findById(1);
    }

    @Test
    public void testFindById_NotFound() {
        when(userRepository.findById(999)).thenReturn(Optional.empty());

        Optional<User> result = userService.findById(999);

        assertFalse(result.isPresent());
        verify(userRepository, times(1)).findById(999);
    }

    @Test
    public void testDelete() {
        doNothing().when(userRepository).deleteById(1);

        userService.delete(1);

        verify(userRepository, times(1)).deleteById(1);
    }


    @Test
    public void testFindByFullName() {
        when(userRepository.findByFullName("Juan Perez")).thenReturn(Optional.of(user1));

        Optional<User> result = userService.findByFullName("Juan Perez");

        assertTrue(result.isPresent());
        assertEquals("Juan Perez", result.get().getFullName());
        verify(userRepository, times(1)).findByFullName("Juan Perez");
    }

    @Test
    public void testFindByFullName_NotFound() {
        when(userRepository.findByFullName("No Existe")).thenReturn(Optional.empty());

        Optional<User> result = userService.findByFullName("No Existe");

        assertFalse(result.isPresent());
        verify(userRepository, times(1)).findByFullName("No Existe");
    }

    @Test
    public void testFindByFullNameContaining() {
        when(userRepository.findByFullNameContaining("Juan")).thenReturn(Arrays.asList(user1));

        List<User> result = userService.findByFullNameContaining("Juan");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Juan Perez", result.get(0).getFullName());
        verify(userRepository, times(1)).findByFullNameContaining("Juan");
    }

    @Test
    public void testFindByFullNameContaining_NotFound() {
        when(userRepository.findByFullNameContaining("Carlos")).thenReturn(Arrays.asList());

        List<User> result = userService.findByFullNameContaining("Carlos");

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(userRepository, times(1)).findByFullNameContaining("Carlos");
    }
}