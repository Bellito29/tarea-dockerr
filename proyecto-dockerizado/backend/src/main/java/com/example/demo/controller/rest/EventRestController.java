package com.example.demo.controller.rest;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.controller.rest.dto.EventRequest;
import com.example.demo.controller.rest.dto.EventResponse;
import com.example.demo.domain.Event;
import com.example.demo.mappers.IEventMapper;
import com.example.demo.service.IEventService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.Parameter;

@RestController
@RequestMapping("/rest/events")
@RequiredArgsConstructor
@Tag(name = "Events", description = "Gestión de eventos")
@SecurityRequirement(name = "bearerAuth")
public class EventRestController {

    private final IEventService eventService;
    private final IEventMapper eventMapper;

    @Operation(summary = "Obtener Event por ID", description = "Retorna un único registro de Event según su identificador")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Event encontrado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = EventResponse.class))),
        @ApiResponse(responseCode = "404", description = "Event no encontrado", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<EventResponse> findById(
            @Parameter(description = "ID del Event", required = true)@PathVariable Integer id) {

        Optional<Event> optionalEvent = eventService.findById(id);

        if (optionalEvent.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        EventResponse response =
                eventMapper.eventToEventResponse(optionalEvent.get());

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "Listar todos los Event", description = "Retorna la lista completa de registros de Event")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente",
            content = @Content(mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = EventResponse.class))))
    })
    @GetMapping
    public ResponseEntity<List<EventResponse>> findAll() {

        List<Event> events = eventService.findAll();

        List<EventResponse> response =
                eventMapper.eventsToEventsResponse(events);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Crear Event", description = "Crea y persiste un nuevo registro de Event")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Event creado exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = EventResponse.class))),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos", content = @Content)
    })
    @PostMapping
    public ResponseEntity<EventResponse> save(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos del Event a crear", required = true,
                content = @Content(schema = @Schema(implementation = EventRequest.class))) @RequestBody EventRequest request) {

        Event event =
                eventMapper.eventRequesToEvent(request);

        Event savedEvent = eventService.save(event);

        EventResponse response =
                eventMapper.eventToEventResponse(savedEvent);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @Operation(summary = "Actualizar Event", description = "Actualiza un registro existente de Event por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Event actualizado exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = EventResponse.class))),
        @ApiResponse(responseCode = "404", description = "Event no encontrado", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<EventResponse> update(
            @Parameter(description = "ID del Event a actualizar", required = true) @PathVariable Integer id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos actualizados del Event", required = true,
                content = @Content(schema = @Schema(implementation = EventRequest.class)))
            @RequestBody EventRequest request) {

        Event event =
                eventMapper.eventRequesToEvent(request);

        Event updatedEvent =
                eventService.update(id, event);

        EventResponse response =
                eventMapper.eventToEventResponse(updatedEvent);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Eliminar Event", description = "Elimina un registro de Event por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Event eliminado exitosamente", content = @Content),
        @ApiResponse(responseCode = "404", description = "Event no encontrado", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@Parameter(description = "ID del Event a eliminar", required = true) 
            @PathVariable Integer id) {

        Optional<Event> optionalEvent =
                eventService.findById(id);

        if (optionalEvent.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        eventService.delete(id);

        return ResponseEntity.noContent().build();
    }
}