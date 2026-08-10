package com.example.demo.controller.rest;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.controller.rest.dto.MessageRequest;
import com.example.demo.controller.rest.dto.MessageResponse;
import com.example.demo.domain.Message;
import com.example.demo.domain.Notification;
import com.example.demo.domain.User;
import com.example.demo.mappers.IMessageMapper;
import com.example.demo.service.IMessageService;
import com.example.demo.service.INotificationService;
import com.example.demo.service.IUserService;

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
@RequestMapping("/rest/messages")
@RequiredArgsConstructor
@Tag(name = "Messages", description = "Gestión de mensajes")
@SecurityRequirement(name = "bearerAuth")
public class MessageRestController {

    private final IMessageService messageService;
    private final IMessageMapper messageMapper;
    private final IUserService userService;
    private final INotificationService notificationService;
    private final SimpMessagingTemplate messagingTemplate;


    @Operation(summary = "Obtener Message por ID", description = "Retorna un único registro de Message según su identificador")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Message encontrado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))),
        @ApiResponse(responseCode = "404", description = "Message no encontrado", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<MessageResponse> findById(@Parameter(description = "ID del Message", required = true) @PathVariable Integer id) {

        Optional<Message> optional = messageService.findById(id);

        if (optional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        MessageResponse response = messageMapper.messageToMessageResponse(optional.get());

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "Listar todos los Message", description = "Retorna la lista completa de registros de Message")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente",
            content = @Content(mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = MessageResponse.class))))
    })
    @GetMapping
    public ResponseEntity<List<MessageResponse>> findAll() {

        List<Message> messages = messageService.findAll();

        List<MessageResponse> response = messageMapper.messagesToMessagesResponse(messages);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Crear Message", description = "Crea y persiste un nuevo registro de Message")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Message creado exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos", content = @Content)
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos del Message a crear", required = true,
        content = @Content(schema = @Schema(implementation = MessageRequest.class)))
    @PostMapping
    public ResponseEntity<MessageResponse> save(@RequestBody MessageRequest request) {

        Message message = messageMapper.messageRequestToMessage(request);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User sender = userService.findByFullName(auth.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        message.setSender(sender);

        User receiver = request.getReceiverId() != null
                ? userService.findById(request.getReceiverId()).orElse(sender)
                : sender;
        message.setReciever(receiver);

        Message savedMessage = messageService.save(message);

        MessageResponse response = messageMapper.messageToMessageResponse(savedMessage);

        messagingTemplate.convertAndSend("/topic/messages", response);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Actualizar Message", description = "Actualiza un registro existente de Message por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Message actualizado exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))),
        @ApiResponse(responseCode = "404", description = "Message no encontrado", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<MessageResponse> update(
            @Parameter(description = "ID del Message a actualizar", required = true) @PathVariable Integer id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos actualizados del Message", required = true,
                content = @Content(schema = @Schema(implementation = MessageRequest.class)))
            @RequestBody MessageRequest request) {

        Optional<Message> optional = messageService.findById(id);

        if (optional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Message existing = optional.get();
        existing.setContent(request.getContent());

        Message updated = messageService.save(existing);

        MessageResponse response = messageMapper.messageToMessageResponse(updated);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Eliminar Message", description = "Elimina un registro de Message por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Message eliminado exitosamente", content = @Content),
        @ApiResponse(responseCode = "404", description = "Message no encontrado", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@Parameter(description = "ID del Message a eliminar", required = true)  @PathVariable Integer id) {

        Optional<Message> optional = messageService.findById(id);

        if (optional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Message message = optional.get();
        Notification notification = message.getNotification();

        if (notification != null) {
            message.setNotification(null);
            messageService.save(message);
            notificationService.deleteById(notification.getId());
        }

        messageService.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
