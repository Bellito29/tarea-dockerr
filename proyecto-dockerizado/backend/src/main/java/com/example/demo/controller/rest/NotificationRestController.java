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

import com.example.demo.controller.rest.dto.NotificationRequest;
import com.example.demo.controller.rest.dto.NotificationResponse;
import com.example.demo.domain.Notification;
import com.example.demo.mappers.INotificationMapper;
import com.example.demo.service.INotificationService;

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
@RequestMapping("/rest/notifications")
@RequiredArgsConstructor
@Tag(name = "Notifications", description = "Gestión de notificaciones")
@SecurityRequirement(name = "bearerAuth")
public class NotificationRestController {

    private final INotificationService notificationService;
    private final INotificationMapper notificationMapper;

    @Operation(summary = "Obtener Notification por ID", description = "Retorna un único registro de Notification según su identificador")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Notification encontrado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = NotificationResponse.class))),
        @ApiResponse(responseCode = "404", description = "Notification no encontrado", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<NotificationResponse> findById(@Parameter(description = "ID del Notification", required = true)@PathVariable Integer id) {

        Optional<Notification> optional = notificationService.findById(id);

        if (optional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        NotificationResponse response = notificationMapper.notificationToNotificationResponse(optional.get());

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "Listar todos los Notification", description = "Retorna la lista completa de registros de Notification")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente",
            content = @Content(mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = NotificationResponse.class))))
    })
    @GetMapping
    public ResponseEntity<List<NotificationResponse>> findAll() {

        List<Notification> notifications = notificationService.findAll();

        List<NotificationResponse> response = notificationMapper.notificationsToNotificationsResponse(notifications);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Crear Notification", description = "Crea y persiste un nuevo registro de Notification")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Notification creado exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = NotificationResponse.class))),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos", content = @Content)
    })
    @PostMapping
    public ResponseEntity<NotificationResponse> save(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos del Notification a crear", required = true,
                content = @Content(schema = @Schema(implementation = NotificationRequest.class)))@RequestBody NotificationRequest request) {

        Notification notification = notificationMapper.notificationRequestToNotification(request);

        Notification savedNotification = notificationService.save(notification);

        NotificationResponse response = notificationMapper.notificationToNotificationResponse(savedNotification);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Actualizar Notification", description = "Actualiza un registro existente de Notification por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Notification actualizado exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = NotificationResponse.class))),
        @ApiResponse(responseCode = "404", description = "Notification no encontrado", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<NotificationResponse> update(@Parameter(description = "ID del Notification a actualizar", required = true) @PathVariable Integer id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos actualizados del Notification", required = true,
                content = @Content(schema = @Schema(implementation = NotificationRequest.class)))
            @RequestBody NotificationRequest request) {

        Optional<Notification> optional = notificationService.findById(id);

        if (optional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Notification existing = optional.get();
        existing.setReferenceId(request.getReferenceId());
        existing.setType(request.getType());

        Notification updated = notificationService.save(existing);

        NotificationResponse response = notificationMapper.notificationToNotificationResponse(updated);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Eliminar Notification", description = "Elimina un registro de Notification por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Notification eliminado exitosamente", content = @Content),
        @ApiResponse(responseCode = "404", description = "Notification no encontrado", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@Parameter(description = "ID del Notification a eliminar", required = true) @PathVariable Integer id) {

        Optional<Notification> optional = notificationService.findById(id);

        if (optional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        notificationService.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
