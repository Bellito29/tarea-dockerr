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

import com.example.demo.controller.rest.dto.TrainerRequest;
import com.example.demo.controller.rest.dto.TrainerResponse;
import com.example.demo.domain.Trainer;
import com.example.demo.mappers.ITrainerMapper;
import com.example.demo.service.ITrainerService;

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
@RequestMapping("/rest/trainers")
@RequiredArgsConstructor
@Tag(name = "Trainers", description = "Gestión de entrenadores")
@SecurityRequirement(name = "bearerAuth")
public class TrainerRestController {

    private final ITrainerService trainerService;
    private final ITrainerMapper trainerMapper;

    @Operation(summary = "Obtener Trainer por ID", description = "Retorna un único registro de Trainer según su identificador")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Trainer encontrado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TrainerResponse.class))),
        @ApiResponse(responseCode = "404", description = "Trainer no encontrado", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<TrainerResponse> findById(@Parameter(description = "ID del Trainer", required = true) @PathVariable Integer id) {

        Optional<Trainer> optional = trainerService.findById(id);

        if (optional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        TrainerResponse response = trainerMapper.trainerToTrainerResponse(optional.get());

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "Listar todos los Trainer", description = "Retorna la lista completa de registros de Trainer")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente",
            content = @Content(mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = TrainerResponse.class))))
    })
    @GetMapping
    public ResponseEntity<List<TrainerResponse>> findAll() {

        List<Trainer> trainers = trainerService.findAll();

        List<TrainerResponse> response = trainerMapper.trainersToTrainersResponse(trainers);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Crear Trainer", description = "Crea y persiste un nuevo registro de Trainer")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Trainer creado exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TrainerResponse.class))),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos", content = @Content)
    })
    @PostMapping
    public ResponseEntity<TrainerResponse> save(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos del Trainer a crear", required = true,
                content = @Content(schema = @Schema(implementation = TrainerRequest.class))) @RequestBody TrainerRequest request) {

        Trainer trainer = trainerMapper.trainerRequestToTrainer(request);

        Trainer savedTrainer = trainerService.save(trainer);

        TrainerResponse response = trainerMapper.trainerToTrainerResponse(savedTrainer);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Actualizar Trainer", description = "Actualiza un registro existente de Trainer por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Trainer actualizado exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TrainerResponse.class))),
        @ApiResponse(responseCode = "404", description = "Trainer no encontrado", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<TrainerResponse> update(
            @Parameter(description = "ID del Trainer a actualizar", required = true) @PathVariable Integer id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos actualizados del Trainer", required = true,
                content = @Content(schema = @Schema(implementation = TrainerRequest.class)))
            @RequestBody TrainerRequest request) {

        Optional<Trainer> optional = trainerService.findById(id);

        if (optional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Trainer existing = optional.get();
        existing.setFullName(request.getFullName());
        existing.setBirthDate(request.getBirthDate());
        existing.setWeight(request.getWeight());
        existing.setHeight(request.getHeight());

        Trainer updated = trainerService.save(existing);

        TrainerResponse response = trainerMapper.trainerToTrainerResponse(updated);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Eliminar Trainer", description = "Elimina un registro de Trainer por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Trainer eliminado exitosamente", content = @Content),
        @ApiResponse(responseCode = "404", description = "Trainer no encontrado", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@Parameter(description = "ID del Trainer a eliminar", required = true) @PathVariable Integer id) {

        Optional<Trainer> optional = trainerService.findById(id);

        if (optional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        trainerService.delete(id);

        return ResponseEntity.noContent().build();
    }
}
