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

import com.example.demo.controller.rest.dto.StatsRequest;
import com.example.demo.controller.rest.dto.StatsResponse;
import com.example.demo.domain.Stats;
import com.example.demo.mappers.IStatsMapper;
import com.example.demo.service.IStatsService;

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
@RequestMapping("/rest/stats")
@RequiredArgsConstructor
@Tag(name = "Stats", description = "Gestión de estadísticas de entrenamiento")
@SecurityRequirement(name = "bearerAuth")
public class StatsRestController {

    private final IStatsService statsService;
    private final IStatsMapper statsMapper;

    @Operation(summary = "Obtener Stats por ID", description = "Retorna un único registro de Stats según su identificador")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Stats encontrado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StatsResponse.class))),
        @ApiResponse(responseCode = "404", description = "Stats no encontrado", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<StatsResponse> findById(@Parameter(description = "ID del Stats", required = true)@PathVariable Integer id) {

        Optional<Stats> optional = statsService.findById(id);

        if (optional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        StatsResponse response = statsMapper.statsToStatsResponse(optional.get());

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "Listar todos los Stats", description = "Retorna la lista completa de registros de Stats")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente",
            content = @Content(mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = StatsResponse.class))))
    })
    @GetMapping
    public ResponseEntity<List<StatsResponse>> findAll() {

        List<Stats> statsList = statsService.findAll();

        List<StatsResponse> response = statsMapper.statsListToStatsResponse(statsList);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Crear Stats", description = "Crea y persiste un nuevo registro de Stats")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Stats creado exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StatsResponse.class))),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos", content = @Content)
    })
    @PostMapping
    public ResponseEntity<StatsResponse> save(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos del Stats a crear", required = true,
                content = @Content(schema = @Schema(implementation = StatsRequest.class)))@RequestBody StatsRequest request) {

        Stats stats = statsMapper.statsRequestToStats(request);

        Stats savedStats = statsService.save(stats);

        StatsResponse response = statsMapper.statsToStatsResponse(savedStats);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Actualizar Stats", description = "Actualiza un registro existente de Stats por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Stats actualizado exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StatsResponse.class))),
        @ApiResponse(responseCode = "404", description = "Stats no encontrado", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<StatsResponse> update(
            @Parameter(description = "ID del Stats a actualizar", required = true) @PathVariable Integer id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos actualizados del Stats", required = true,
                content = @Content(schema = @Schema(implementation = StatsRequest.class)))
            @RequestBody StatsRequest request) {

        Optional<Stats> optional = statsService.findById(id);

        if (optional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Stats existing = optional.get();
        existing.setTime(request.getTime());
        existing.setAmmountExercises(request.getAmmountExercises());

        Stats updated = statsService.save(existing);

        StatsResponse response = statsMapper.statsToStatsResponse(updated);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Eliminar Stats", description = "Elimina un registro de Stats por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Stats eliminado exitosamente", content = @Content),
        @ApiResponse(responseCode = "404", description = "Stats no encontrado", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@Parameter(description = "ID del Stats a eliminar", required = true)@PathVariable Integer id) {

        Optional<Stats> optional = statsService.findById(id);

        if (optional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        statsService.delete(id);

        return ResponseEntity.noContent().build();
    }
}
