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

import com.example.demo.controller.rest.dto.StudentRequest;
import com.example.demo.controller.rest.dto.StudentResponse;
import com.example.demo.domain.Student;
import com.example.demo.mappers.IStudentMapper;
import com.example.demo.service.IStudentService;

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
@RequestMapping("/rest/students")
@RequiredArgsConstructor
@Tag(name = "Students", description = "Gestión de estudiantes")
@SecurityRequirement(name = "bearerAuth")
public class StudentRestController {

    private final IStudentService studentService;
    private final IStudentMapper studentMapper;

    @Operation(summary = "Obtener Student por ID", description = "Retorna un único registro de Student según su identificador")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Student encontrado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StudentResponse.class))),
        @ApiResponse(responseCode = "404", description = "Student no encontrado", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<StudentResponse> findById(@Parameter(description = "ID del Student", required = true)@PathVariable Integer id) {

        Optional<Student> optional = studentService.findById(id);

        if (optional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        StudentResponse response = studentMapper.studentToStudentResponse(optional.get());

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "Listar todos los Student", description = "Retorna la lista completa de registros de Student")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente",
            content = @Content(mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = StudentResponse.class))))
    })
    @GetMapping
    public ResponseEntity<List<StudentResponse>> findAll() {

        List<Student> students = studentService.findAll();

        List<StudentResponse> response = studentMapper.studentsToStudentsResponse(students);

        return ResponseEntity.ok(response);
    }

     @Operation(summary = "Crear Student", description = "Crea y persiste un nuevo registro de Student")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Student creado exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StudentResponse.class))),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos", content = @Content)
    })
    @PostMapping
    public ResponseEntity<StudentResponse> save(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos del Student a crear", required = true,
                content = @Content(schema = @Schema(implementation = StudentRequest.class))) @RequestBody StudentRequest request) {

        Student student = studentMapper.studentRequestToStudent(request);

        Student savedStudent = studentService.save(student);

        StudentResponse response = studentMapper.studentToStudentResponse(savedStudent);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Actualizar Student", description = "Actualiza un registro existente de Student por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Student actualizado exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StudentResponse.class))),
        @ApiResponse(responseCode = "404", description = "Student no encontrado", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<StudentResponse> update(
            @Parameter(description = "ID del Student a actualizar", required = true) @PathVariable Integer id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos actualizados del Student", required = true,
                content = @Content(schema = @Schema(implementation = StudentRequest.class)))
            @RequestBody StudentRequest request) {

        Optional<Student> optional = studentService.findById(id);

        if (optional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Student existing = optional.get();
        existing.setFullName(request.getFullName());
        existing.setBirthDate(request.getBirthDate());
        existing.setWeight(request.getWeight());
        existing.setHeight(request.getHeight());

        Student updated = studentService.save(existing);

        StudentResponse response = studentMapper.studentToStudentResponse(updated);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Eliminar Student", description = "Elimina un registro de Student por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Student eliminado exitosamente", content = @Content),
        @ApiResponse(responseCode = "404", description = "Student no encontrado", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@Parameter(description = "ID del Student a eliminar", required = true) @PathVariable Integer id) {

        Optional<Student> optional = studentService.findById(id);

        if (optional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        studentService.delete(id);

        return ResponseEntity.noContent().build();
    }
}
