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
import com.example.demo.domain.Message;
import com.example.demo.domain.User;
import com.example.demo.service.impl.NotificationService;
import com.example.demo.domain.Notification;
import com.example.demo.Repository.INotificationRepository;


@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class NotificationServiceTest {

    @Mock
    private INotificationRepository notificationRepository;

    @InjectMocks
    private NotificationService notificationService;

    private Notification notification;
    private Notification savedNotification;
    private User user;
    private Message message;

    @BeforeEach
    public void setUp() {

        //User
        user = new User();
        user.setId(1);
        user.setFullName("Test User");
        user.setBirthDate("2000-01-01");
        user.setWeight(70.0);
        user.setHeight(1.75);
        user.setPassword("1234");

        //Message
        message = new Message();
        message.setId(10);
        message.setContent("Hola");
        
        //Notification sin ID
        notification = new Notification();
        notification.setId(null);
        notification.setReferenceId(99);
        notification.setType("MESSAGE");
        notification.setUser(user);
        notification.setMessage(message);

        //Notification guardada
        savedNotification = new Notification();
        savedNotification.setId(1);
        savedNotification.setReferenceId(99);
        savedNotification.setType("MESSAGE");
        savedNotification.setUser(user);
        savedNotification.setMessage(message);
    }

    @Test
    public void testFindAll() {
        when(notificationRepository.findAll())
                .thenReturn(Arrays.asList(savedNotification));

        List<Notification> notifications = notificationService.findAll();

        assertNotNull(notifications);
        assertEquals(1, notifications.size());
        verify(notificationRepository, times(1)).findAll();
    }

    @Test
    public void testFindById() {
        when(notificationRepository.findById(1))
                .thenReturn(Optional.of(savedNotification));

        Notification result = notificationService.findById(1)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        assertNotNull(result);
        assertEquals(1, result.getId());
        verify(notificationRepository, times(1)).findById(1);
    }

    @Test
    public void testFindByIdNotFound() {
        when(notificationRepository.findById(1))
                .thenReturn(Optional.empty());

        Optional<Notification> result = notificationService.findById(1);

        assertTrue(result.isEmpty());
    }

    @Test
    public void testSave() {
        when(notificationRepository.save(any(Notification.class)))
                .thenReturn(savedNotification);

        Notification result = notificationService.save(notification);

        assertNotNull(result);
        assertEquals("MESSAGE", result.getType());
        assertEquals(1, result.getUser().getId());
        verify(notificationRepository, times(1)).save(any(Notification.class));
    }

    @Test
    public void testDeleteById() {
        doNothing().when(notificationRepository).deleteById(1);
        when(notificationRepository.findById(1))
                .thenReturn(Optional.empty());

        notificationService.deleteById(1);

        Optional<Notification> result = notificationService.findById(1);

        assertTrue(result.isEmpty());
    }
}
