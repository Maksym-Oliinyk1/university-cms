package ua.com.foxminded.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.com.foxminded.dto.StudentDTO;

import java.util.UUID;

@RestController
@RequestMapping("/student")
public interface StudentController {

    @Operation(summary = "Save new student", description = "Creates and saves a new student")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Student created",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = StudentDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content)
    })
    @PostMapping
    ResponseEntity<StudentDTO> save(
            @RequestBody @Valid StudentDTO studentDTO);

    @Operation(summary = "Update existing student", description = "Updates details of an existing student")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Student updated",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = StudentDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Student not found",
                    content = @Content)
    })
    @PutMapping("/{studentId}")
    ResponseEntity<StudentDTO> update(
            @RequestBody @Valid StudentDTO studentDTO,
            @PathVariable UUID studentId);

    @Operation(summary = "Get student by ID", description = "Fetches a student by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Student found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = StudentDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Student not found",
                    content = @Content)
    })
    @GetMapping("/{studentId}")
    ResponseEntity<StudentDTO> get(@PathVariable UUID studentId);

    @Operation(summary = "Delete student", description = "Deletes a student by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Student deleted"),
            @ApiResponse(responseCode = "404", description = "Student not found",
                    content = @Content)
    })
    @DeleteMapping("/{studentId}")
    ResponseEntity<Void> delete(@PathVariable UUID studentId);
}
