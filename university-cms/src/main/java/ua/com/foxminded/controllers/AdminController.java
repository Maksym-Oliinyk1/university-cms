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
import ua.com.foxminded.dto.AdministratorDTO;

import java.util.UUID;

@RestController
@RequestMapping("/admin")
public interface AdminController {

    @Operation(
            summary = "Save new administrator",
            description = "Creates and saves a new administrator")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Administrator created",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = AdministratorDTO.class))
                            }),
                    @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content)
            })
    @PostMapping
    ResponseEntity<AdministratorDTO> save(@RequestBody @Valid AdministratorDTO administratorDTO);

    @Operation(
            summary = "Update existing administrator",
            description = "Updates details of an existing administrator")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Administrator updated",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = AdministratorDTO.class))
                            }),
                    @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Administrator not found",
                            content = @Content)
            })
    @PutMapping
    ResponseEntity<AdministratorDTO> update(
            @RequestBody @Valid AdministratorDTO administratorDTO, @PathVariable @Valid UUID adminId);

    @Operation(
            summary = "Get administrator by ID",
            description = "Fetches an administrator by its ID")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Administrator found",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = AdministratorDTO.class))
                            }),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Administrator not found",
                            content = @Content)
            })
    @GetMapping
    ResponseEntity<AdministratorDTO> get(@PathVariable @Valid UUID adminId);

    @Operation(summary = "Delete administrator", description = "Deletes an administrator by its ID")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Administrator deleted"),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Administrator not found",
                            content = @Content)
            })
    @DeleteMapping
    ResponseEntity<Void> delete(@PathVariable @Valid UUID adminId);
}
