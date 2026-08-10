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
import com.example.demo.service.impl.MessageService;
import com.example.demo.domain.Notification;
import com.example.demo.Repository.IMessageRepository;


@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class MessageServiceTest {

    @Mock
    private IMessageRepository messageRepository;

    @InjectMocks
    private MessageService messageService;

    private Message message;
    private Message savedMessage;
    private User sender;
    private User receiver;
    private Notification notification;

@BeforeEach
public void setUp() {

    //Sender
    sender = new User();
    sender.setId(1);
    sender.setFullName("Sender User");
    sender.setBirthDate("2000-01-01");
    sender.setWeight(70.0);
    sender.setHeight(1.75);
    sender.setPassword("1234");

    //Receiver
    receiver = new User();
    receiver.setId(2);
    receiver.setFullName("Receiver User");
    receiver.setBirthDate("1999-05-10");
    receiver.setWeight(65.0);
    receiver.setHeight(1.65);
    receiver.setPassword("abcd");

    //Message sin ID
    message = new Message();
    message.setId(null);
    message.setContent("Hola mundo");
    message.setSender(sender);
    message.setReciever(receiver);

    //Notification
    notification = new Notification();
    notification.setId(1);
    notification.setReferenceId(99);
    notification.setType("MESSAGE");
    notification.setUser(receiver); // normalmente el receptor recibe la notificación

    //Conexion de ambos lados
    notification.setMessage(message);
    message.setNotification(notification);

    //Message guardado
    savedMessage = new Message();
    savedMessage.setId(10);
    savedMessage.setContent("Hola mundo");
    savedMessage.setSender(sender);
    savedMessage.setReciever(receiver);

    Notification savedNotification = new Notification();
    savedNotification.setId(2);
    savedNotification.setReferenceId(100);
    savedNotification.setType("MESSAGE");
    savedNotification.setUser(receiver);

    //conectar también en el objeto guardado
    savedNotification.setMessage(savedMessage);
    savedMessage.setNotification(savedNotification);
}

    @Test
    public void testFindAll() {
        when(messageRepository.findAll()).thenReturn(Arrays.asList(savedMessage));

        List<Message> messages = messageService.findAll();

        assertNotNull(messages);
        assertEquals(1, messages.size());
        verify(messageRepository, times(1)).findAll();
    }

    @Test
    public void testFindById() {
        when(messageRepository.findById(10)).thenReturn(Optional.of(savedMessage));

        Message result = messageService.findById(10).orElseThrow(() -> 
    new RuntimeException("Message not found"));

        assertNotNull(result);
        assertEquals(10, result.getId());
        verify(messageRepository, times(1)).findById(10);
    }

    @Test
public void testFindByIdNotFound() {
    when(messageRepository.findById(10))
            .thenReturn(Optional.empty());

    Optional<Message> result = messageService.findById(10);

    assertTrue(result.isEmpty());
}

    @Test
    public void testSave() {
        when(messageRepository.save(any(Message.class))).thenReturn(savedMessage);

        Message result = messageService.save(message);

        assertNotNull(result);
        assertEquals("Hola mundo", result.getContent());
        assertEquals(1, result.getSender().getId());
        verify(messageRepository, times(1)).save(any(Message.class));
    }

    @Test
    public void testDeleteById() {
        doNothing().when(messageRepository).deleteById(10);
        when(messageRepository.findById(10)).thenReturn(Optional.empty());

        messageService.deleteById(10);

        Optional<Message> result = messageService.findById(10);

        assertTrue(result.isEmpty());

    }

}