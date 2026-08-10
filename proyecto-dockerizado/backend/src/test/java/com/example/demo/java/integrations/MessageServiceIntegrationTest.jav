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
import com.example.demo.domain.Notification;
import com.example.demo.Repository.IMessageRepository;
import com.example.demo.Service.impl.MessageService;

import jakarta.transaction.Transactional;
@SpringBootTest
@ActiveProfiles("demo")
@Transactional
public class MessageServiceIntegrationTest {

    @Autowired
    private IMessageService messageService;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private INotificationRepository notificationRepository;

    private User sender;
    private User receiver;

    private User buildUser(String name) {
        User user = new User();
        user.setFullName(name);
        user.setBirthDate("2000-01-01");
        user.setWeight(70.0);
        user.setHeight(1.75);
        user.setPassword("1234");
        return user;
    }

    private Message buildMessage(String content, User sender, User receiver) {
        Message message = new Message();
        message.setContent(content);
        message.setSender(sender);
        message.setReciever(receiver);
        return message;
    }

    private Notification buildNotification(User user, Message message) {
        Notification notification = new Notification();
        notification.setReferenceId(1);
        notification.setType("MESSAGE");
        notification.setUser(user);
        notification.setMessage(message);
        return notification;
    }
    @Test
    public void testFindAll() {
        Message message = buildMessage("Hola mundo", sender, receiver);
        messageService.save(message);

        List<Message> messages = messageService.findAll();

        assertNotNull(messages);
        assertEquals(1, messages.size());
    }

    @Test
    public void testFindById() {
        Message message = buildMessage("Hola mundo", sender, receiver);
        Message saved = messageService.save(message);

        Optional<Message> result = messageService.findById(saved.getId());

        assertTrue(result.isPresent());
        assertEquals(saved.getId(), result.get().getId());
    }

    @Test
    public void testSave() {
        Message message = buildMessage("Hola mundo", sender, receiver);

        Message saved = messageService.save(message);

        assertNotNull(saved);
        assertNotNull(saved.getId());
        assertEquals("Hola mundo", saved.getContent());
        assertEquals(sender.getId(), saved.getSender().getId());
    }

    @Test
    public void testDelete() {
        Message message = buildMessage("Hola mundo", sender, receiver);
        Message saved = messageService.save(message);

        Integer id = saved.getId();
        messageService.deleteById(id);

        Optional<Message> result = messageService.findById(id);

        assertFalse(result.isPresent());
    }

    @Test
    public void testFindByIdNotFound() {
        Optional<Message> result = messageService.findById(999);

        assertTrue(result.isEmpty());
    }
}