package com.example.demo.java.integrations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.example.demo.domain.Admin;
import com.example.demo.domain.Event;
import com.example.demo.service.IAdminService;
import com.example.demo.service.IEventService;

import jakarta.transaction.Transactional;

@SpringBootTest
@ActiveProfiles("demo")
@Transactional
public class EventServiceIntegrationTest {

    @Autowired
    private IEventService eventService;

    @Autowired
    private IAdminService adminService;

    private Event buildValidEvent() {
        Admin admin = new Admin();
        admin.setFullName("Admin Event");
        admin.setPassword("123");
        admin.setBirthDate("2000-01-01");
        admin.setWeight(70.0);
        admin.setHeight(1.75);

        admin = adminService.save(admin);

        Event event = new Event();
        event.setTitle("Evento Test");
        event.setPlace("Lugar Test");
        event.setDate(LocalDateTime.now());
        event.setAdmin(admin);

        return event;
    }

    @Test
    public void testFindAll() {
        eventService.save(buildValidEvent());

        List<Event> events = eventService.findAll();

        assertNotNull(events);
        assertEquals(1, events.size());
    }

    @Test
    public void testFindById() {
        Event event = eventService.save(buildValidEvent());

        Optional<Event> result = eventService.findById(event.getId());

        assertTrue(result.isPresent());
        assertEquals(event.getId(), result.get().getId());
    }

    @Test
    public void testSave() {
        Event saved = eventService.save(buildValidEvent());

        assertNotNull(saved);
        assertNotNull(saved.getId());
    }

    @Test
    public void testDelete() {
        Event event = eventService.save(buildValidEvent());

        eventService.delete(event.getId());

        Optional<Event> result = eventService.findById(event.getId());
        assertFalse(result.isPresent());
    }
}