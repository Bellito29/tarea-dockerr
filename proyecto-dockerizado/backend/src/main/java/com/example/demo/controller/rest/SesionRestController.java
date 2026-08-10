package com.example.demo.controller.rest;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

import com.example.demo.controller.rest.dto.SesionRequest;
import com.example.demo.controller.rest.dto.SesionResponse;
import com.example.demo.domain.Sesion;
import com.example.demo.domain.User;
import com.example.demo.mappers.ISesionMapper;
import com.example.demo.service.ISesionService;
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
@RequestMapping("/rest/sesions")
@RequiredArgsConstructor
@Tag(name = "Sesions", description = "Gestión de sesiones de entrenamiento")
@SecurityRequirement(name = "bearerAuth")
public class SesionRestController {

    private final ISesionService sesionService;
    private final ISesionMapper sesionMapper;
    private final IUserService userService;

    @Operation(summary = "Obtener Sesion por ID", description = "Retorna un único registro de Sesion según su identificador")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sesion encontrado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = SesionResponse.class))),
        @ApiResponse(responseCode = "404", description = "Sesion no encontrado", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<SesionResponse> findById(@Parameter(description = "ID del Sesion", required = true)@PathVariable Integer id) {

        Optional<Sesion> optional = sesionService.findById(id);

        if (optional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        SesionResponse response = sesionMapper.sesionToSesionResponse(optional.get());

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "Listar todos los Sesion", description = "Retorna la lista completa de registros de Sesion")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente",
            content = @Content(mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = SesionResponse.class))))
    })
    @GetMapping
    public ResponseEntity<List<SesionResponse>> findAll() {

        List<Sesion> sesions = sesionService.findAll();

        List<SesionResponse> response = sesionMapper.sesionsToSesionsResponse(sesions);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Crear Sesion", description = "Crea y persiste un nuevo registro de Sesion")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Sesion creado exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = SesionResponse.class))),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos", content = @Content)
    })
    @PostMapping
    public ResponseEntity<SesionResponse> save(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos del Sesion a crear", required = true,
                content = @Content(schema = @Schema(implementation = SesionRequest.class)))@RequestBody SesionRequest request) {

        Sesion sesion = sesionMapper.sesionRequestToSesion(request);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userService.findByFullName(auth.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        sesion.setUser(currentUser);

        Sesion savedSesion = sesionService.save(sesion);

        SesionResponse response = sesionMapper.sesionToSesionResponse(savedSesion);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Actualizar Sesion", description = "Actualiza un registro existente de Sesion por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sesion actualizado exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = SesionResponse.class))),
        @ApiResponse(responseCode = "404", description = "Sesion no encontrado", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<SesionResponse> update(
            @Parameter(description = "ID del Sesion a actualizar", required = true) @PathVariable Integer id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos actualizados del Sesion", required = true,
                content = @Content(schema = @Schema(implementation = SesionRequest.class)))
            @RequestBody SesionRequest request) {

        Optional<Sesion> optional = sesionService.findById(id);

        if (optional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Sesion existing = optional.get();
        existing.setSesionDate(request.getSesionDate());
        existing.setDuration(request.getDuration());

        Sesion updated = sesionService.save(existing);

        SesionResponse response = sesionMapper.sesionToSesionResponse(updated);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Eliminar Sesion", description = "Elimina un registro de Sesion por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Sesion eliminado exitosamente", content = @Content),
        @ApiResponse(responseCode = "404", description = "Sesion no encontrado", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@Parameter(description = "ID del Sesion a eliminar", required = true) @PathVariable Integer id) {

        Optional<Sesion> optional = sesionService.findById(id);

        if (optional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        sesionService.delete(id);

        return ResponseEntity.noContent().build();
    }
}
