package com.example.demo.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.Repository.IEventRepository;
import com.example.demo.domain.Event;
import com.example.demo.service.IEventService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class EventService implements IEventService {
    private final IEventRepository eventRepository;

    @Override
    public List<Event> findAll() {
        return eventRepository.findAll();
    }

    @Override
    public Optional<Event> findById(Integer id) {
        return eventRepository.findById(id);
    }

    @Override
    public Event save(Event event) {
        return eventRepository.save(event);
    }

    @Override
    public void delete(Integer id) {
        eventRepository.deleteById(id);
    }

    @Override
    public Event update(Integer id, Event event) {

        Event existingEvent = eventRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Evento no encontrado"));

        existingEvent.setTitle(event.getTitle());
        existingEvent.setPlace(event.getPlace());
        existingEvent.setDate(event.getDate());

        return eventRepository.save(existingEvent);
    }
}
