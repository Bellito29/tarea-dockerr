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

import com.example.demo.controller.rest.dto.UserRequest;
import com.example.demo.controller.rest.dto.UserResponse;
import com.example.demo.domain.User;
import com.example.demo.mappers.IUserMapper;
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
@RequestMapping("/rest/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "Gestión de usuarios")
@SecurityRequirement(name = "bearerAuth")
public class UserRestController {

    private final IUserService userService;
    private final IUserMapper userMapper;
    
    @Operation(summary = "Obtener User por ID", description = "Retorna un único registro de User según su identificador")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User encontrado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class))),
        @ApiResponse(responseCode = "404", description = "User no encontrado", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> findById(
            @Parameter(description = "ID del User", required = true) @PathVariable Integer id) {

        Optional<User> optionalUser = userService.findById(id);

        if (optionalUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        UserResponse response = userMapper.userToUserResponse(optionalUser.get());

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "Listar todos los User", description = "Retorna la lista completa de registros de User")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente",
            content = @Content(mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = UserResponse.class))))
    })
    @GetMapping
    public ResponseEntity<List<UserResponse>> findAll() {
    
        List<User> users = userService.findAll();
    
        List<UserResponse> response =
                userMapper.usersToUsersResponse(users);
    
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Crear User", description = "Crea y persiste un nuevo registro de User")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "User creado exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class))),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos", content = @Content)
    })
    @PostMapping
    public ResponseEntity<UserResponse> save(@RequestBody UserRequest request) {
        User savedUser = userService.save(request);          // ← directo, sin mapper aquí
        UserResponse response = userMapper.userToUserResponse(savedUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

   @Operation(summary = "Actualizar User", description = "Actualiza un registro existente de User por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User actualizado exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class))),
        @ApiResponse(responseCode = "404", description = "User no encontrado", content = @Content)
    }) 
    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> update(@PathVariable Integer id,
                                            @RequestBody UserRequest request) {
        User updatedUser = userService.update(id, request);  // ← directo
        UserResponse response = userMapper.userToUserResponse(updatedUser);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Eliminar User", description = "Elimina un registro de User por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "User eliminado exitosamente", content = @Content),
        @ApiResponse(responseCode = "404", description = "User no encontrado", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@Parameter(description = "ID del User a eliminarr", required = true) @PathVariable Integer id) {

        Optional<User> optionalUser = userService.findById(id);

        if (optionalUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        userService.delete(id);

        return ResponseEntity.noContent().build();
    }
}
