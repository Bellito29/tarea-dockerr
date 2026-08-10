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

import com.example.demo.domain.Message;
import com.example.demo.domain.User;
import com.example.demo.service.impl.NotificationService;
import com.example.demo.domain.Notification;
import com.example.demo.Repository.IMessageRepository;
import com.example.demo.Repository.IUserRepository;

import jakarta.transaction.Transactional;

@SpringBootTest
@ActiveProfiles("demo")
@Transactional
public class NotificationServiceIntegrationTest {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private IMessageRepository messageRepository;

    @Autowired
    private IUserRepository userRepository;

    // -------- BUILDERS LIMPIOS (NO GUARDAN) --------

    private User buildUser(String name) {
        User u = new User();
        u.setFullName(name);
        u.setBirthDate("2000-01-01");
        u.setWeight(70.0);
        u.setHeight(1.75);
        u.setPassword("1234");
        return u;
    }

    private Message buildMessage(User sender, User receiver) {
        Message m = new Message();
        m.setContent("Hola");
        m.setSender(sender);
        m.setReciever(receiver);
        return m;
    }

    private Notification buildNotification(User user, Message message) {
        Notification n = new Notification();
        n.setReferenceId(99);
        n.setType("MESSAGE");
        n.setUser(user);
        return n;
    }

    // -------- TESTS --------

    @Test
    public void testFindAll() {
        User sender = userRepository.save(buildUser("Sender"));
        User receiver = userRepository.save(buildUser("Receiver"));

        Message message = messageRepository.save(buildMessage(sender, receiver));

        Notification saved = notificationService.save(
                buildNotification(receiver, message)
        );
        List<Notification> notifications = notificationService.findAll();

        assertNotNull(notifications);
        assertEquals(1, notifications.size());
    }

    @Test
    public void testFindById() {
        User sender = userRepository.save(buildUser("Sender"));
        User receiver = userRepository.save(buildUser("Receiver"));

        Message message = messageRepository.save(buildMessage(sender, receiver));

        Notification saved = notificationService.save(
                buildNotification(receiver, message)
        );

        Optional<Notification> result = notificationService.findById(saved.getId());

        assertTrue(result.isPresent());
        assertEquals(saved.getId(), result.get().getId());
    }

    @Test
    public void testFindByIdNotFound() {
        Optional<Notification> result = notificationService.findById(999);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testSave() {
        User sender = userRepository.save(buildUser("Sender"));
        User receiver = userRepository.save(buildUser("Receiver"));

        Message message = messageRepository.save(buildMessage(sender, receiver));

        Notification saved = notificationService.save(
                buildNotification(receiver, message)
        );

        assertNotNull(saved);
        assertNotNull(saved.getId());
        assertEquals("MESSAGE", saved.getType());
        assertEquals(receiver.getId(), saved.getUser().getId());
    }

    @Test
    public void testDelete() {
        User sender = userRepository.save(buildUser("Sender"));
        User receiver = userRepository.save(buildUser("Receiver"));

        Message message = messageRepository.save(buildMessage(sender, receiver));

        Notification saved = notificationService.save(
                buildNotification(receiver, message)
        );

        Integer id = saved.getId();
        notificationService.deleteById(id);

        Optional<Notification> result = notificationService.findById(id);

        assertFalse(result.isPresent());
    }
}