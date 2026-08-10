package com.example.demo.controller.rest;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.controller.rest.dto.LoginRequest;
import com.example.demo.service.IAuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.example.demo.controller.rest.dto.TokenResponse;



@RestController
@RequestMapping("/rest/public/auth")
@Tag(name = "Auth", description = "Operaciones de autenticación pública")
public class AuthRestController {
        @Autowired
        private IAuthService authService;

        @Operation(
        summary = "Iniciar sesión",
        description = "Autentica al usuario con sus credenciales y retorna un token JWT de acceso. " +
                      "Este endpoint es público y no requiere autenticación previa."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Autenticación exitosa. Retorna el token JWT",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TokenResponse.class))),
        @ApiResponse(responseCode = "401", description = "Credenciales incorrectas o usuario no encontrado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class)))
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Credenciales del usuario", required = true,
        content = @Content(schema = @Schema(implementation = LoginRequest.class)))
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
            try {
                TokenResponse token = authService.login(request);
                return ResponseEntity.ok(token);

        } catch (Exception e) {
                return ResponseEntity.status(401).body(e.getMessage());

        }
    }
}
