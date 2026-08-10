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

import com.example.demo.controller.rest.dto.RecomendationRequest;
import com.example.demo.controller.rest.dto.RecomendationResponse;
import com.example.demo.domain.Recomendation;
import com.example.demo.mappers.IRecomendationMapper;
import com.example.demo.service.IRecomendationService;

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
@RequestMapping("/rest/recomendations")
@RequiredArgsConstructor
@Tag(name = "Recomendations", description = "Gestión de recomendaciones")
@SecurityRequirement(name = "bearerAuth")
public class RecomendationRestController {

    private final IRecomendationService recomendationService;
    private final IRecomendationMapper recomendationMapper;


    @Operation(summary = "Obtener Recomendation por ID", description = "Retorna un único registro de Recomendation según su identificador")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Recomendation encontrado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = RecomendationResponse.class))),
        @ApiResponse(responseCode = "404", description = "Recomendation no encontrado", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<RecomendationResponse> findById(@Parameter(description = "ID del Recomendation", required = true) @PathVariable Integer id) {

        Optional<Recomendation> optional = recomendationService.findById(id);

        if (optional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        RecomendationResponse response =
                recomendationMapper.recomendationToRecomendationResponse(optional.get());

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "Listar todos los Recomendation", description = "Retorna la lista completa de registros de Recomendation")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente",
            content = @Content(mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = RecomendationResponse.class))))
    })
    @GetMapping
    public ResponseEntity<List<RecomendationResponse>> findAll() {

        List<Recomendation> recomendations = recomendationService.findAll();

        List<RecomendationResponse> response =
                recomendationMapper.recomendationsToRecomendationsResponse(recomendations);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Crear Recomendation", description = "Crea y persiste un nuevo registro de Recomendation")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Recomendation creado exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = RecomendationResponse.class))),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos", content = @Content)
    })
    @PostMapping
    public ResponseEntity<RecomendationResponse> save(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos del Recomendation a crear", required = true,
                content = @Content(schema = @Schema(implementation = RecomendationRequest.class)))@RequestBody RecomendationRequest request) {

        Recomendation recomendation =
                recomendationMapper.recomendationRequestToRecomendation(request);

        Recomendation saved = recomendationService.save(recomendation);

        RecomendationResponse response =
                recomendationMapper.recomendationToRecomendationResponse(saved);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Actualizar Recomendation", description = "Actualiza un registro existente de Recomendation por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Recomendation actualizado exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = RecomendationResponse.class))),
        @ApiResponse(responseCode = "404", description = "Recomendation no encontrado", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<RecomendationResponse> update(
            @Parameter(description = "ID del Recomendation a actualizar", required = true) @PathVariable Integer id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos actualizados del Recomendation", required = true,
                content = @Content(schema = @Schema(implementation = RecomendationRequest.class)))
            @RequestBody RecomendationRequest request) {

        Optional<Recomendation> optional = recomendationService.findById(id);

        if (optional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Recomendation existing = optional.get();
        existing.setDescription(request.getDescription());

        Recomendation updated = recomendationService.save(existing);

        RecomendationResponse response =
                recomendationMapper.recomendationToRecomendationResponse(updated);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Eliminar Recomendation", description = "Elimina un registro de Recomendation por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Recomendation eliminado exitosamente", content = @Content),
        @ApiResponse(responseCode = "404", description = "Recomendation no encontrado", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@Parameter(description = "ID del Recomendation a eliminar", required = true)@PathVariable Integer id) {

        Optional<Recomendation> optional = recomendationService.findById(id);

        if (optional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        recomendationService.delete(id);

        return ResponseEntity.noContent().build();
    }
}
