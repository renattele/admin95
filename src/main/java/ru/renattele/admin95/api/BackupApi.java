package ru.renattele.admin95.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.renattele.admin95.dto.BackupDto;
import ru.renattele.admin95.form.BackupDeleteQueryForm;

import java.util.List;

@RequestMapping("/api/admin/backup")
@PreAuthorize("hasRole('ACCESS_BACKUPS')")
@Tag(name = "Backup Management", description = "API for managing system backups")
public interface BackupApi {

    @Operation(summary = "List all backups", description = "Retrieves a list of all system backups")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved backup list",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BackupDto.class)))
    })
    @GetMapping
    List<BackupDto> getBackups();

    @Operation(summary = "Download backup file", description = "Downloads a specific backup file by ID and name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Backup file downloaded successfully",
                    content = @Content(mediaType = "application/octet-stream")),
            @ApiResponse(responseCode = "404", description = "Backup file not found")
    })
    @GetMapping(value = "/{id}/{name}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ResponseBody
    ResponseEntity<Resource> downloadBackup(
            @Parameter(description = "Backup ID") @PathVariable("id") Long id,
            @Parameter(description = "Backup filename") @PathVariable("name") String name);

    @Operation(summary = "Count backups for deletion", description = "Returns the count of backups matching deletion criteria")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Count retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Integer.class))),
            @ApiResponse(responseCode = "400", description = "Invalid deletion criteria provided")
    })
    @PostMapping(value = "/delete-count-query", consumes = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    int getDeleteCount(
            @Parameter(description = "Deletion criteria form") @RequestBody BackupDeleteQueryForm form);

    @Operation(summary = "Create backup", description = "Initiates the creation of a new system backup")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Backup creation initiated successfully"),
            @ApiResponse(responseCode = "500", description = "Server error during backup creation")
    })
    @PostMapping
    @ResponseBody
    void createBackup();

    @Operation(summary = "Delete backup", description = "Deletes a specific backup by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Backup deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Backup not found")
    })
    @DeleteMapping("/{id}")
    @ResponseBody
    void deleteBackup(
            @Parameter(description = "ID of the backup to delete") @PathVariable("id") Long id);

    @Operation(summary = "Delete backups by query", description = "Deletes multiple backups based on query criteria")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Matching backups deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid deletion query provided")
    })
    @DeleteMapping(value = "/query", consumes = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    void deleteBackupByQuery(
            @Parameter(description = "Query criteria for backups to delete") @RequestBody BackupDeleteQueryForm form);
}
