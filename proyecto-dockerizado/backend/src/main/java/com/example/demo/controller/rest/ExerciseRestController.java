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

import com.example.demo.controller.rest.dto.ExerciseRequest;
import com.example.demo.controller.rest.dto.ExerciseResponse;
import com.example.demo.domain.Exercise;
import com.example.demo.mappers.IExerciseMapper;
import com.example.demo.service.IExerciceService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.Parameter;

@RestController
@RequestMapping("/rest/exercises")
@RequiredArgsConstructor
@Tag(name = "Exercises", description = "Gestión de ejercicios")
public class ExerciseRestController {

    private final IExerciceService exerciseService;
    private final IExerciseMapper exerciseMapper;

    @Operation(summary = "Obtener Exercise por ID", description = "Retorna un único registro de Exercise según su identificador")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Exercise encontrado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExerciseResponse.class))),
        @ApiResponse(responseCode = "404", description = "Exercise no encontrado", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<ExerciseResponse> findById( @Parameter(description = "ID del Exercise", required = true) @PathVariable Integer id) {

        Optional<Exercise> optional = exerciseService.findById(id);

        if (optional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        ExerciseResponse response = exerciseMapper.exerciseToExerciseResponse(optional.get());

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "Listar todos los Exercise", description = "Retorna la lista completa de registros de Exercise")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente",
            content = @Content(mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = ExerciseResponse.class))))
    })
    @GetMapping
    public ResponseEntity<List<ExerciseResponse>> findAll() {

        List<Exercise> exercises = exerciseService.findAll();

        List<ExerciseResponse> response = exerciseMapper.exercisesToExercisesResponse(exercises);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Crear Exercise", description = "Crea y persiste un nuevo registro de Exercise")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Exercise creado exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExerciseResponse.class))),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos", content = @Content)
    })
    @PostMapping
    public ResponseEntity<ExerciseResponse> save(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos del Exercise a crear", required = true,
                content = @Content(schema = @Schema(implementation = ExerciseRequest.class)))@RequestBody ExerciseRequest request) {

        Exercise exercise = exerciseMapper.exerciseRequestToExercise(request);

        Exercise savedExercise = exerciseService.save(exercise);

        ExerciseResponse response = exerciseMapper.exerciseToExerciseResponse(savedExercise);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Actualizar Exercise", description = "Actualiza un registro existente de Exercise por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Exercise actualizado exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExerciseResponse.class))),
        @ApiResponse(responseCode = "404", description = "Exercise no encontrado", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<ExerciseResponse> update(
            @Parameter(description = "ID del Exercise a actualizar", required = true) @PathVariable Integer id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos actualizados del Exercise", required = true,
                content = @Content(schema = @Schema(implementation = ExerciseRequest.class)))
            @RequestBody ExerciseRequest request) {

        Optional<Exercise> optional = exerciseService.findById(id);

        if (optional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Exercise existing = optional.get();
        existing.setName(request.getName());
        existing.setType(request.getType());
        existing.setDescription(request.getDescription());
        existing.setDuration(request.getDuration());
        existing.setDifficulty(request.getDifficulty());
        existing.setVideoUrl(request.getVideoUrl());

        Exercise updated = exerciseService.save(existing);

        ExerciseResponse response = exerciseMapper.exerciseToExerciseResponse(updated);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Eliminar Exercise", description = "Elimina un registro de Exercise por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Exercise eliminado exitosamente", content = @Content),
        @ApiResponse(responseCode = "404", description = "Exercise no encontrado", content = @Content)
    })
    @DeleteMapping("/{id}")
    @Parameter(description = "ID del Exercise a eliminar", required = true)
    public ResponseEntity<Void> deleteById(@PathVariable Integer id) {

        Optional<Exercise> optional = exerciseService.findById(id);

        if (optional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        exerciseService.delete(id);

        return ResponseEntity.noContent().build();
    }
}
