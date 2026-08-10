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

import com.example.demo.controller.rest.dto.AdminRequest;
import com.example.demo.controller.rest.dto.AdminResponse;
import com.example.demo.domain.Admin;
import com.example.demo.mappers.IAdminMapper;
import com.example.demo.service.IAdminService;

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
@RequestMapping("/rest/admins")
@RequiredArgsConstructor
@Tag(name = "Admins", description = "Gestión de administradores")
@SecurityRequirement(name = "bearerAuth")
public class AdminRestController {

    private final IAdminService adminService;
    private final IAdminMapper adminMapper;

    @Operation(summary = "Obtener Admin por ID", description = "Retorna un único registro de Admin según su identificador")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Admin encontrado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AdminResponse.class))),
        @ApiResponse(responseCode = "404", description = "Admin no encontrado", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<AdminResponse> findById(@Parameter(description = "ID del Admin", required = true)@PathVariable Integer id) {

        Optional<Admin> optional = adminService.findById(id);

        if (optional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        AdminResponse response = adminMapper.adminToAdminResponse(optional.get());

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "Listar todos los Admin", description = "Retorna la lista completa de registros de Admin")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente",
            content = @Content(mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = AdminResponse.class))))
    })
    @GetMapping
    public ResponseEntity<List<AdminResponse>> findAll() {

        List<Admin> admins = adminService.findAll();

        List<AdminResponse> response = adminMapper.adminsToAdminsResponse(admins);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Crear Admin", description = "Crea y persiste un nuevo registro de Admin")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Admin creado exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AdminResponse.class))),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos", content = @Content)
    })
    @PostMapping
    public ResponseEntity<AdminResponse> save(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos del Admin a crear", required = true,
                content = @Content(schema = @Schema(implementation = AdminRequest.class)))@RequestBody AdminRequest request) {

        Admin admin = adminMapper.adminRequestToAdmin(request);

        Admin savedAdmin = adminService.save(admin);

        AdminResponse response = adminMapper.adminToAdminResponse(savedAdmin);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Actualizar Admin", description = "Actualiza un registro existente de Admin por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Admin actualizado exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AdminResponse.class))),
        @ApiResponse(responseCode = "404", description = "Admin no encontrado", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<AdminResponse> update(@Parameter(description = "ID del Admin a actualizar", required = true) @PathVariable Integer id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos actualizados del Admin", required = true,
                content = @Content(schema = @Schema(implementation = AdminRequest.class)))
            @RequestBody AdminRequest request) {

        Optional<Admin> optional = adminService.findById(id);

        if (optional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Admin existing = optional.get();
        existing.setFullName(request.getFullName());
        existing.setBirthDate(request.getBirthDate());
        existing.setWeight(request.getWeight());
        existing.setHeight(request.getHeight());

        Admin updated = adminService.save(existing);

        AdminResponse response = adminMapper.adminToAdminResponse(updated);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Eliminar Admin", description = "Elimina un registro de Admin por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Admin eliminado exitosamente", content = @Content),
        @ApiResponse(responseCode = "404", description = "Admin no encontrado", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@Parameter(description = "ID del Admin a eliminar", required = true) @PathVariable Integer id) {

        Optional<Admin> optional = adminService.findById(id);

        if (optional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        adminService.delete(id);

        return ResponseEntity.noContent().build();
    }
}
