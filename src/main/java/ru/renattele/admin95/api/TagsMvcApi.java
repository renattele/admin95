package ru.renattele.admin95.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotBlank;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.renattele.admin95.dto.DockerProjectDto;
import ru.renattele.admin95.dto.TagDto;
import ru.renattele.admin95.validation.NameRegexPattern;

import java.lang.annotation.*;
import java.util.Set;

/**
 * Interface defining the REST API for tag management operations
 */
@Tag(name = "Tags", description = "Tag management operations")
@RequestMapping("/admin/tags")
@PreAuthorize("hasRole('ACCESS_CONTAINERS')")
public interface TagsMvcApi {

    /**
     * Retrieves all available tags
     *
     * @return Set of all tags
     */
    @Operation(summary = "Get all tags", description = "Retrieves a complete list of all available tags")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "List of tags successfully retrieved",
                content = @Content(array = @ArraySchema(schema = @Schema(implementation = TagDto.class))))
    })
    @GetMapping("")
    Set<TagDto> allTags();

    /**
     * Retrieves all tags associated with a specific project
     *
     * @param projectName Name of the project
     * @return Set of tags associated with the project
     */
    @Operation(summary = "Get tags for project", description = "Retrieves all tags associated with the specified project")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "List of tags successfully retrieved"),
        @ApiResponse(responseCode = "400", description = "Invalid project name format"),
        @ApiResponse(responseCode = "404", description = "Project not found")
    })
    @GetMapping("/tags-by-project/{name}")
    Set<TagDto> tagsForProject(
            @Parameter(description = "Name of the project", required = true)
            @PathVariable("name") @NameRegexPattern String projectName);

    /**
     * Retrieves all projects associated with a specific tag
     *
     * @param tagName Name of the tag
     * @return Set of projects associated with the tag
     */
    @Operation(summary = "Get projects for tag", description = "Retrieves all projects associated with the specified tag")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "List of projects successfully retrieved"),
        @ApiResponse(responseCode = "400", description = "Invalid tag name format"),
        @ApiResponse(responseCode = "404", description = "Tag not found")
    })
    @GetMapping("/projects-by-tag/{name}")
    Set<DockerProjectDto> projectsForTag(
            @Parameter(description = "Name of the tag", required = true)
            @PathVariable("name") @TagPattern String tagName);

    /**
     * Creates a new tag
     *
     * @param name Name of the tag to create
     */
    @Operation(summary = "Create tag", description = "Creates a new tag with the specified name")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tag successfully created"),
        @ApiResponse(responseCode = "400", description = "Invalid tag name format"),
        @ApiResponse(responseCode = "409", description = "Tag already exists")
    })
    @PostMapping("/{name}")
    void createTag(
            @Parameter(description = "Name of the tag to create", required = true)
            @PathVariable("name") @TagPattern String name);

    /**
     * Deletes an existing tag
     *
     * @param name Name of the tag to delete
     */
    @Operation(summary = "Delete tag", description = "Deletes the specified tag")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tag successfully deleted"),
        @ApiResponse(responseCode = "400", description = "Invalid tag name format"),
        @ApiResponse(responseCode = "404", description = "Tag not found")
    })
    @DeleteMapping("/{name}")
    void deleteTag(
            @Parameter(description = "Name of the tag to delete", required = true)
            @PathVariable("name") @TagPattern String name);

    /**
     * Adds a tag to a specific project
     *
     * @param projectName Name of the project
     * @param name        Name of the tag
     */
    @Operation(summary = "Add tag to project", description = "Associates the specified tag with the specified project")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tag successfully added to project"),
        @ApiResponse(responseCode = "400", description = "Invalid project or tag name format"),
        @ApiResponse(responseCode = "404", description = "Project or tag not found")
    })
    @PatchMapping("/{project}/{name}/add")
    void addTagToProject(
            @Parameter(description = "Name of the project", required = true)
            @PathVariable("project") @NameRegexPattern String projectName,
            @Parameter(description = "Name of the tag", required = true)
            @PathVariable("name") @TagPattern String name);

    /**
     * Removes a tag from a specific project
     *
     * @param projectName Name of the project
     * @param name        Name of the tag
     */
    @Operation(summary = "Remove tag from project", description = "Removes the association between the specified tag and project")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tag successfully removed from project"),
        @ApiResponse(responseCode = "400", description = "Invalid project or tag name format"),
        @ApiResponse(responseCode = "404", description = "Project, tag or association not found")
    })
    @PatchMapping("/{project}/{name}/remove")
    void removeTagFromProject(
            @Parameter(description = "Name of the project", required = true)
            @PathVariable("project") @NameRegexPattern String projectName,
            @Parameter(description = "Name of the tag", required = true)
            @PathVariable("name") @TagPattern String name);

    @Documented
    @Constraint(validatedBy = {})
    @Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @NotBlank
    @interface TagPattern {

        /**
         * Error message to be used when validation fails.
         */
        String message() default "Invalid name format";

        /**
         * Validation groups, allowing validation to be applied only if
         * the current validation belongs to one of these groups.
         */
        Class<?>[] groups() default {};

        /**
         * Payloads, which can be used to carry metadata information consumed by
         * validation clients.
         */
        Class<? extends Payload>[] payload() default {};
    }
}
