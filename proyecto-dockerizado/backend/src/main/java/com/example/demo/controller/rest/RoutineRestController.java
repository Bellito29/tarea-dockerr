package com.example.demo.controller.rest;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.controller.rest.dto.RoutineRequest;
import com.example.demo.controller.rest.dto.RoutineResponse;
import com.example.demo.domain.Routine;
import com.example.demo.domain.Trainer;
import com.example.demo.domain.User;
import com.example.demo.mappers.IRoutineMapper;
import com.example.demo.service.IRoutineService;
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
@RequestMapping("/rest/routines")
@RequiredArgsConstructor
@Tag(name = "Routines", description = "Gestión de rutinas de ejercicio")
@SecurityRequirement(name = "bearerAuth")
public class RoutineRestController {

    private final IRoutineService routineService;
    private final IRoutineMapper routineMapper;
    private final IUserService userService;

    @Operation(summary = "Obtener Routine por ID", description = "Retorna un único registro de Routine según su identificador")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Routine encontrado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = RoutineResponse.class))),
        @ApiResponse(responseCode = "404", description = "Routine no encontrado", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<RoutineResponse> findById(@Parameter(description = "ID del Routine", required = true)@PathVariable Integer id) {

        Optional<Routine> optional = routineService.findById(id);

        if (optional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        RoutineResponse response = routineMapper.routineToRoutineResponse(optional.get());

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "Listar todos los Routine", description = "Retorna la lista completa de registros de Routine")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente",
            content = @Content(mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = RoutineResponse.class))))
    })
    @GetMapping
    public ResponseEntity<List<RoutineResponse>> findAll() {

        List<Routine> routines = routineService.findAll();

        List<RoutineResponse> response = routineMapper.routinesToRoutinesResponse(routines);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Crear Routine", description = "Crea y persiste un nuevo registro de Routine")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Routine creado exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = RoutineResponse.class))),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos", content = @Content)
    })
    @PostMapping
    public ResponseEntity<RoutineResponse> save(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos del Routine a crear", required = true,
                content = @Content(schema = @Schema(implementation = RoutineRequest.class)))@RequestBody RoutineRequest request) {

        Routine routine = routineMapper.routineRequestToRoutine(request);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userService.findByFullName(auth.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        routine.setUser(currentUser);
        if (currentUser instanceof Trainer trainer) {
            routine.setTrainer(trainer);
        }

        Routine savedRoutine = routineService.save(routine);

        RoutineResponse response = routineMapper.routineToRoutineResponse(savedRoutine);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Actualizar Routine", description = "Actualiza un registro existente de Routine por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Routine actualizado exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = RoutineResponse.class))),
        @ApiResponse(responseCode = "404", description = "Routine no encontrado", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<RoutineResponse> update(
            @Parameter(description = "ID del Routine a actualizar", required = true) @PathVariable Integer id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos actualizados del Routine", required = true,
                content = @Content(schema = @Schema(implementation = RoutineRequest.class)))
            @RequestBody RoutineRequest request) {

        Optional<Routine> optional = routineService.findById(id);

        if (optional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Routine existing = optional.get();
        existing.setRoutineName(request.getRoutineName());
        existing.setDescription(request.getDescription());
        existing.setVisibility(request.getVisibility());

        Routine updated = routineService.save(existing);

        RoutineResponse response = routineMapper.routineToRoutineResponse(updated);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Eliminar Routine", description = "Elimina un registro de Routine por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Routine eliminado exitosamente", content = @Content),
        @ApiResponse(responseCode = "404", description = "Routine no encontrado", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@Parameter(description = "ID del Routine a eliminar", required = true) @PathVariable Integer id) {

        Optional<Routine> optional = routineService.findById(id);

        if (optional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        routineService.delete(id);

        return ResponseEntity.noContent().build();
    }
}
