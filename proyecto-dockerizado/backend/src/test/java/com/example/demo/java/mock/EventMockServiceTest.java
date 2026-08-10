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

import java.time.LocalDateTime;
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

import com.example.demo.Repository.IEventRepository;
import com.example.demo.domain.Admin;
import com.example.demo.domain.Event;
import com.example.demo.service.impl.EventService;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class EventMockServiceTest {

    @Mock
    private IEventRepository eventRepository;

    @InjectMocks
    private EventService eventService;

    private Event event;
    private Event savedEvent;
    private Admin admin;

    @BeforeEach
    public void setUp() {
        admin = new Admin();
        admin.setId(1);
        admin.setFullName("Admin");

        event = new Event();
        event.setId(null);
        event.setTitle("Evento");
        event.setPlace("Lugar");
        event.setDate(LocalDateTime.now());
        event.setAdmin(admin);

        savedEvent = new Event();
        savedEvent.setId(10);
        savedEvent.setTitle("Evento");
        savedEvent.setPlace("Lugar");
        savedEvent.setDate(LocalDateTime.now());
        savedEvent.setAdmin(admin);
    }

    @Test
    public void testFindAll() {
        when(eventRepository.findAll()).thenReturn(Arrays.asList(savedEvent));

        List<Event> result = eventService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(eventRepository, times(1)).findAll();
    }

    @Test
    public void testFindById() {
        when(eventRepository.findById(10)).thenReturn(Optional.of(savedEvent));

        Optional<Event> result = eventService.findById(10);

        assertTrue(result.isPresent());
        assertEquals(10, result.get().getId());
        verify(eventRepository, times(1)).findById(10);
    }

    @Test
    public void testFindByIdNotFound() {
        when(eventRepository.findById(10)).thenReturn(Optional.empty());

        Optional<Event> result = eventService.findById(10);

        assertFalse(result.isPresent());
        verify(eventRepository, times(1)).findById(10);
    }

    @Test
    public void testSave() {
        when(eventRepository.save(any(Event.class))).thenReturn(savedEvent);

        Event result = eventService.save(event);

        assertNotNull(result);
        assertEquals(10, result.getId());
        verify(eventRepository, times(1)).save(any(Event.class));
    }

    @Test
    public void testDelete() {
        doNothing().when(eventRepository).deleteById(10);

        eventService.delete(10);

        verify(eventRepository, times(1)).deleteById(10);
    }
}