package com.example.demo.controller.rest;

import java.util.List;

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

import com.example.demo.controller.rest.dto.RolePermissionRequest;
import com.example.demo.controller.rest.dto.RolePermissionResponse;
import com.example.demo.domain.RolePermission;
import com.example.demo.mappers.IRolePermissionMapper;
import com.example.demo.service.RolePermissionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.Parameter;

@RestController
@RequestMapping("/rest/role-permissions")
@RequiredArgsConstructor
@Tag(name = "RolePermissions", description = "Gestión de permisos por rol")
@SecurityRequirement(name = "bearerAuth")
public class RolePermissionRestController {

    private final RolePermissionService rolePermissionService;
    private final IRolePermissionMapper rolePermissionMapper;

    @Operation(summary = "Obtener RolePermission por ID", description = "Retorna un único registro de RolePermission según su identificador")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "RolePermission encontrado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = RolePermissionResponse.class))),
        @ApiResponse(responseCode = "404", description = "RolePermission no encontrado", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<RolePermissionResponse> findById(@Parameter(description = "ID del RolePermission", required = true) @PathVariable Long id) {

        try {
            RolePermission rolePermission = rolePermissionService.findById(id);
            RolePermissionResponse response =
                    rolePermissionMapper.rolePermissionToRolePermissionResponse(rolePermission);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Listar todos los RolePermission", description = "Retorna la lista completa de registros de RolePermission")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente",
            content = @Content(mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = RolePermissionResponse.class))))
    })
    @GetMapping
    public ResponseEntity<List<RolePermissionResponse>> findAll() {

        List<RolePermission> rolePermissions = rolePermissionService.findAll();

        List<RolePermissionResponse> response =
                rolePermissionMapper.rolePermissionsToRolePermissionsResponse(rolePermissions);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Crear RolePermission", description = "Crea y persiste un nuevo registro de RolePermission")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "RolePermission creado exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = RolePermissionResponse.class))),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos", content = @Content)
    })
    @PostMapping
    public ResponseEntity<RolePermissionResponse> save(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos del RolePermission a crear", required = true,
                content = @Content(schema = @Schema(implementation = RolePermissionRequest.class)))@RequestBody RolePermissionRequest request) {

        RolePermission rolePermission =
                rolePermissionMapper.rolePermissionRequestToRolePermission(request);

        RolePermission saved = rolePermissionService.save(rolePermission);

        RolePermissionResponse response =
                rolePermissionMapper.rolePermissionToRolePermissionResponse(saved);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Actualizar RolePermission", description = "Actualiza un registro existente de RolePermission por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "RolePermission actualizado exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = RolePermissionResponse.class))),
        @ApiResponse(responseCode = "404", description = "RolePermission no encontrado", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<RolePermissionResponse> update(
            @Parameter(description = "ID del RolePermission a actualizar", required = true) @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos actualizados del RolePermission", required = true,
                content = @Content(schema = @Schema(implementation = RolePermissionRequest.class)))
            @RequestBody RolePermissionRequest request) {

        RolePermission rolePermission =
                rolePermissionMapper.rolePermissionRequestToRolePermission(request);

        RolePermission updated = rolePermissionService.update(id, rolePermission);

        RolePermissionResponse response =
                rolePermissionMapper.rolePermissionToRolePermissionResponse(updated);

        return ResponseEntity.ok(response);
    }

     @Operation(summary = "Eliminar RolePermission", description = "Elimina un registro de RolePermission por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "RolePermission eliminado exitosamente", content = @Content),
        @ApiResponse(responseCode = "404", description = "RolePermission no encontrado", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@Parameter(description = "ID del RolePermission a eliminar", required = true) @PathVariable Long id) {

        RolePermission rolePermission = rolePermissionService.findById(id);

        if (rolePermission == null) {
            return ResponseEntity.notFound().build();
        }

        rolePermissionService.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
