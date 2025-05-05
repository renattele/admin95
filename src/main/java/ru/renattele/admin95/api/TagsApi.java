package ru.renattele.admin95.api;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.web.bind.annotation.*;
import ru.renattele.admin95.dto.DockerProjectDto;
import ru.renattele.admin95.dto.TagDto;
import ru.renattele.admin95.model.TagEntity;
import ru.renattele.admin95.model.docker.DockerProjectEntity;
import ru.renattele.admin95.util.RegexConstants;
import ru.renattele.admin95.validation.NameRegexPattern;

import java.lang.annotation.*;
import java.util.Set;

/**
 * Interface defining the REST API for tag management operations
 */
@RequestMapping("/admin/tags")
public interface TagsApi {

    /**
     * Retrieves all available tags
     *
     * @return Set of all tags
     */
    @GetMapping("")
    Set<TagDto> allTags();

    /**
     * Retrieves all tags associated with a specific project
     *
     * @param projectName Name of the project
     * @return Set of tags associated with the project
     */
    @GetMapping("/projects-by-tag/{name}")
    Set<TagDto> tagsForProject(@PathVariable("name") @NameRegexPattern String projectName);

    /**
     * Retrieves all projects associated with a specific tag
     *
     * @param tagName Name of the tag
     * @return Set of projects associated with the tag
     */
    @GetMapping("/tags-by-project/{name}")
    Set<DockerProjectDto> projectsForTag(@PathVariable("name") @TagPattern String tagName);

    /**
     * Creates a new tag
     *
     * @param name Name of the tag to create
     */
    @PostMapping("/{name}")
    void createTag(@PathVariable("name") @TagPattern String name);

    /**
     * Deletes an existing tag
     *
     * @param name Name of the tag to delete
     */
    @DeleteMapping("/{name}")
    void deleteTag(@PathVariable("name") @TagPattern String name);

    /**
     * Adds a tag to a specific project
     *
     * @param projectName Name of the project
     * @param name        Name of the tag
     */
    @PatchMapping("/{project}/{name}/add")
    void addTagToProject(@PathVariable("project") @NameRegexPattern String projectName,
                         @PathVariable("name") @TagPattern String name);

    /**
     * Removes a tag from a specific project
     *
     * @param projectName Name of the project
     * @param name        Name of the tag
     */
    @PatchMapping("/{project}/{name}/remove")
    void removeTagFromProject(@PathVariable("project") @NameRegexPattern String projectName,
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