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
import ua.com.foxminded.dto.TeacherDTO;

import java.util.UUID;

@RestController
@RequestMapping("/teacher")
public interface TeacherController {

    @Operation(summary = "Save new teacher", description = "Creates and saves a new teacher")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Teacher created",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = TeacherDTO.class))
                            }),
                    @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content)
            })
    @PostMapping
    ResponseEntity<TeacherDTO> save(@RequestBody @Valid TeacherDTO teacherDTO);

    @Operation(
            summary = "Update existing teacher",
            description = "Updates details of an existing teacher")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Teacher updated",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = TeacherDTO.class))
                            }),
                    @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Teacher not found", content = @Content)
            })
    @PutMapping("/{teacherId}")
    ResponseEntity<TeacherDTO> update(
            @RequestBody @Valid TeacherDTO teacherDTO, @PathVariable UUID teacherId);

    @Operation(summary = "Get teacher by ID", description = "Fetches a teacher by its ID")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Teacher found",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = TeacherDTO.class))
                            }),
                    @ApiResponse(responseCode = "404", description = "Teacher not found", content = @Content)
            })
    @GetMapping("/{teacherId}")
    ResponseEntity<TeacherDTO> findById(@PathVariable UUID teacherId);

    @Operation(summary = "Delete teacher", description = "Deletes a teacher by its ID")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Teacher deleted"),
                    @ApiResponse(responseCode = "404", description = "Teacher not found", content = @Content)
            })
    @DeleteMapping("/{teacherId}")
    ResponseEntity<Void> delete(@PathVariable UUID teacherId);
}
