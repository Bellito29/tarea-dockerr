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

import com.example.demo.controller.rest.dto.RoleRequest;
import com.example.demo.controller.rest.dto.RoleResponse;
import com.example.demo.domain.Role;
import com.example.demo.mappers.IRoleMapper;
import com.example.demo.service.RoleService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/rest/roles")
@RequiredArgsConstructor
@Tag(name = "Roles", description = "Gestión de roles")
@SecurityRequirement(name = "bearerAuth")
public class RoleRestController {

    private final RoleService roleService;
    private final IRoleMapper roleMapper;

    @Operation(summary = "Obtener Role por ID", description = "Retorna un único registro de Role según su identificador")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Role encontrado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = RoleResponse.class))),
        @ApiResponse(responseCode = "404", description = "Role no encontrado", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<RoleResponse> findById( @Parameter(description = "ID del Role", required = true) @PathVariable Long id) {

        try {
            Role role = roleService.findById(id);
            RoleResponse response = roleMapper.roleToRoleResponse(role);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Listar todos los Role", description = "Retorna la lista completa de registros de Role")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente",
            content = @Content(mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = RoleResponse.class))))
    })
    @GetMapping
    public ResponseEntity<List<RoleResponse>> findAll() {

        List<Role> roles = roleService.findAll();

        List<RoleResponse> response = roleMapper.rolesToRolesResponse(roles);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Actualizar Role", description = "Actualiza un registro existente de Role por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Role actualizado exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = RoleResponse.class))),
        @ApiResponse(responseCode = "404", description = "Role no encontrado", content = @Content)
    })
    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<RoleResponse> update(
            @Parameter(description = "ID del Role a actualizar", required = true) @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos actualizados del Role", required = true,
                content = @Content(schema = @Schema(implementation = RoleRequest.class)))
            @RequestBody RoleRequest request) {

        Role role = roleMapper.roleRequestToRole(request);
        roleService.update(id, role);
        Role reloaded = roleService.findById(id);  // ← carga con EntityGraph
        return ResponseEntity.ok(roleMapper.roleToRoleResponse(reloaded));
    }

    @Operation(summary = "Eliminar Role", description = "Elimina un registro de Role por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Role eliminado exitosamente", content = @Content),
        @ApiResponse(responseCode = "404", description = "Role no encontrado", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@Parameter(description = "ID del Role a eliminar", required = true) @PathVariable Long id) {

        Role role = roleService.findById(id);

        if (role == null) {
            return ResponseEntity.notFound().build();
        }

        roleService.deleteById(id);

        return ResponseEntity.noContent().build();
    }

    @PostMapping
    @Transactional
    public ResponseEntity<RoleResponse> createRole(@Valid @RequestBody RoleRequest request) {
        Role roleEntity = roleMapper.roleRequestToRole(request);
        Role savedRole = roleService.save(roleEntity);
        // Forzamos la carga del rol con sus permisos dentro de la misma sesión
        Role reloaded = roleService.findById(savedRole.getId());
        return ResponseEntity.ok(roleMapper.roleToRoleResponse(reloaded));
    }
}
