package ru.renattele.admin95.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.URL;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.renattele.admin95.util.RegexConstants;
import ru.renattele.admin95.validation.NameRegexPattern;

/**
 * Interface for operations related to container management
 */
@RequestMapping("/admin/containers")
public interface ContainersApi {

    /**
     * Display container dashboard
     *
     * @param name  optional container name
     * @param model view model
     * @return view name
     */
    @GetMapping(value = {"/{name}", "", "/"})
    String containers(@PathVariable(required = false)
                      @NameRegexPattern(message = "Name should be valid") String name,
                      Model model);

    /**
     * Create a new project service
     *
     * @param name service name
     */
    @PostMapping(value = "/service/{name}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    void createProject(@PathVariable
                       @NameRegexPattern(message = "Invalid project name")
                       String name);

    /**
     * Create a new file
     *
     * @param name file name
     */
    @PostMapping(value = "/file/{name}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    void createFile(@PathVariable
                    @NameRegexPattern(message = "Invalid file name")
                    String name);

    /**
     * Start a project
     *
     * @param name project name
     */
    @PostMapping(value = "/{name}/start")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    void startProject(@PathVariable
                      @NameRegexPattern(message = "Invalid project name")
                      String name);

    /**
     * Stop a project
     *
     * @param name project name
     */
    @PostMapping(value = "/{name}/stop")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    void stopProject(@PathVariable
                     @NameRegexPattern(message = "Invalid project name")
                     String name);

    /**
     * Save file content
     *
     * @param name    file name
     * @param rawBody request containing link and content
     * @return response with status
     */
    @PutMapping(value = "/{name}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    ResponseEntity<String> saveFile(
            @PathVariable
            @NameRegexPattern(message = "Invalid file name")
            String name,
            @RequestBody(required = false) @Valid SaveFileRequest rawBody);

    /**
     * Delete a file
     *
     * @param name file name
     * @return response with status
     */
    @DeleteMapping(value = "/{name}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    ResponseEntity<String> deleteFile(@PathVariable
                                      @NameRegexPattern(message = "Invalid file name")
                                      String name);

    /**
     * Request payload for saving file content
     */
    record SaveFileRequest(@URL String link, String content) {
    }
}