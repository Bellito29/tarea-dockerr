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

import com.example.demo.controller.rest.dto.PermissionRequest;
import com.example.demo.controller.rest.dto.PermissionResponse;
import com.example.demo.domain.Permission;
import com.example.demo.mappers.IPermissionMapper;
import com.example.demo.service.IPermissionService;

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
@RequestMapping("/rest/permissions")
@RequiredArgsConstructor
@Tag(name = "Permissions", description = "Gestión de permisos")
@SecurityRequirement(name = "bearerAuth")
public class PermissionRestController {

    private final IPermissionService permissionService;
    private final IPermissionMapper permissionMapper;

    @Operation(summary = "Obtener Permission por ID", description = "Retorna un único registro de Permission según su identificador")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Permission encontrado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PermissionResponse.class))),
        @ApiResponse(responseCode = "404", description = "Permission no encontrado", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<PermissionResponse> findById(@Parameter(description = "ID del Permission a buscar", required = true) @PathVariable Integer id) {

        Optional<Permission> optional = permissionService.findById(id);

        if (optional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        PermissionResponse response = permissionMapper.permissionToPermissionResponse(optional.get());

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "Listar todos los Permission", description = "Retorna la lista completa de registros de Permission")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente",
            content = @Content(mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = PermissionResponse.class))))
    })
    @GetMapping
    public ResponseEntity<List<PermissionResponse>> findAll() {

        List<Permission> permissions = permissionService.findAll();

        List<PermissionResponse> response = permissionMapper.permissionsToPermissionsResponse(permissions);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Crear Permission", description = "Crea y persiste un nuevo registro de Permission")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Permission creado exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PermissionResponse.class))),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos", content = @Content)
    })
    @PostMapping
    public ResponseEntity<PermissionResponse> save(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos del Permission a crear", required = true,
                content = @Content(schema = @Schema(implementation = PermissionRequest.class)))@RequestBody PermissionRequest request) {

        Permission permission = permissionMapper.permissionRequestToPermission(request);

        Permission savedPermission = permissionService.save(permission);

        PermissionResponse response = permissionMapper.permissionToPermissionResponse(savedPermission);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Actualizar Permission", description = "Actualiza un registro existente de Permission por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Permission actualizado exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PermissionResponse.class))),
        @ApiResponse(responseCode = "404", description = "Permission no encontrado", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<PermissionResponse> update(
            @Parameter(description = "ID del Permission a actualizar", required = true) @PathVariable Integer id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos actualizados del Permission", required = true,
                content = @Content(schema = @Schema(implementation = PermissionRequest.class)))
            @RequestBody PermissionRequest request) {

        Optional<Permission> optional = permissionService.findById(id);

        if (optional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Permission existing = optional.get();
        existing.setCode(request.getCode());
        existing.setName(request.getName());
        existing.setDescription(request.getDescription());

        Permission updated = permissionService.save(existing);

        PermissionResponse response = permissionMapper.permissionToPermissionResponse(updated);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Eliminar Permission", description = "Elimina un registro de Permission por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Permission eliminado exitosamente", content = @Content),
        @ApiResponse(responseCode = "404", description = "Permission no encontrado", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@Parameter(description = "ID del Permission a eliminar", required = true) @PathVariable Integer id) {

        Optional<Permission> optional = permissionService.findById(id);

        if (optional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        permissionService.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
