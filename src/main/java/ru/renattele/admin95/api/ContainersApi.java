package ru.renattele.admin95.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.hibernate.validator.constraints.URL;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.renattele.admin95.validation.NameRegexPattern;

/**
 * Interface for operations related to container management
 */
@Tag(name = "Containers", description = "Container management operations")
@RequestMapping("/admin/containers")
public interface ContainersApi {

    /**
     * Display container dashboard
     *
     * @param name  optional container name
     * @param model view model
     * @return view name
     */
    @Operation(summary = "Display container dashboard", description = "Returns the container dashboard view, optionally filtered by container name")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved the dashboard"),
        @ApiResponse(responseCode = "400", description = "Invalid container name format")
    })
    @GetMapping(value = {"/{name}", "", "/"})
    String containers(@Parameter(description = "Container name to filter by") 
                      @PathVariable(required = false)
                      @NameRegexPattern(message = "Name should be valid") String name,
                      Model model);

    /**
     * Create a new project service
     *
     * @param name service name
     */
    @Operation(summary = "Create a new project service", description = "Creates a new service with the specified name")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Service successfully created"),
        @ApiResponse(responseCode = "400", description = "Invalid project name format")
    })
    @PostMapping(value = "/service/{name}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    void createProject(@Parameter(description = "Name of the service to create", required = true)
                       @PathVariable
                       @NameRegexPattern(message = "Invalid project name")
                       String name);

    /**
     * Create a new file
     *
     * @param name file name
     */
    @Operation(summary = "Create a new file", description = "Creates a new file with the specified name")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "File successfully created"),
        @ApiResponse(responseCode = "400", description = "Invalid file name format")
    })
    @PostMapping(value = "/file/{name}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    void createFile(@Parameter(description = "Name of the file to create", required = true)
                    @PathVariable
                    @NameRegexPattern(message = "Invalid file name")
                    String name);

    /**
     * Start a project
     *
     * @param name project name
     */
    @Operation(summary = "Start a project", description = "Starts the specified project container")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Project successfully started"),
        @ApiResponse(responseCode = "400", description = "Invalid project name format"),
        @ApiResponse(responseCode = "404", description = "Project not found")
    })
    @PostMapping(value = "/{name}/start")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    void startProject(@Parameter(description = "Name of the project to start", required = true)
                      @PathVariable
                      @NameRegexPattern(message = "Invalid project name")
                      String name);

    /**
     * Stop a project
     *
     * @param name project name
     */
    @Operation(summary = "Stop a project", description = "Stops the specified project container")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Project successfully stopped"),
        @ApiResponse(responseCode = "400", description = "Invalid project name format"),
        @ApiResponse(responseCode = "404", description = "Project not found")
    })
    @PostMapping(value = "/{name}/stop")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    void stopProject(@Parameter(description = "Name of the project to stop", required = true)
                     @PathVariable
                     @NameRegexPattern(message = "Invalid project name")
                     String name);

    /**
     * Save file content
     *
     * @param name    file name
     * @param rawBody request containing link and content
     * @return response with status
     */
    @Operation(summary = "Save file content", description = "Updates the content of a specified file")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "File content successfully saved"),
        @ApiResponse(responseCode = "400", description = "Invalid file name format or request body"),
        @ApiResponse(responseCode = "404", description = "File not found")
    })
    @PutMapping(value = "/{name}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    ResponseEntity<String> saveFile(
            @Parameter(description = "Name of the file to update", required = true)
            @PathVariable
            @NameRegexPattern(message = "Invalid file name")
            String name,
            @Parameter(description = "Request body containing file URL and content")
            @RequestBody(required = false) @Valid SaveFileRequest rawBody);

    /**
     * Delete a file
     *
     * @param name file name
     * @return response with status
     */
    @Operation(summary = "Delete a file", description = "Deletes the specified file")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "File successfully deleted"),
        @ApiResponse(responseCode = "400", description = "Invalid file name format"),
        @ApiResponse(responseCode = "404", description = "File not found")
    })
    @DeleteMapping(value = "/{name}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    ResponseEntity<String> deleteFile(@Parameter(description = "Name of the file to delete", required = true)
                                      @PathVariable
                                      @NameRegexPattern(message = "Invalid file name")
                                      String name);

    /**
     * Request payload for saving file content
     */
    @Schema(description = "Request for saving file content")
    record SaveFileRequest(
            @Schema(description = "URL link to the file", example = "https://example.com/file.txt") 
            @URL String link,
            
            @Schema(description = "Content of the file", example = "File content goes here") 
            String content) {
    }
}
