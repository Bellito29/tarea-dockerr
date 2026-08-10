package com.example.demo.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import com.example.demo.controller.rest.dto.EventRequest;
import com.example.demo.controller.rest.dto.EventResponse;
import com.example.demo.domain.Event;

@Mapper(componentModel = "spring")
public interface IEventMapper {

    EventResponse eventToEventResponse(Event event);
    List<EventResponse> eventsToEventsResponse(List<Event> events); 
    Event eventRequesToEvent(EventRequest eventRequest);
}
