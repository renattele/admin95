package ru.renattele.admin95.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.renattele.admin95.model.FileEntity;
import ru.renattele.admin95.repository.FileRepository;

@Controller
@RequestMapping("/admin/containers")
@RequiredArgsConstructor
public class ContainersController {
    private final FileRepository fileRepository;

    @GetMapping(value = {"/{name}", ""})
    public String dashboard(
            @PathVariable(required = false) String name,
            Model model
    ) {
        var files = fileRepository.getFiles();
        var currentFile = files.stream()
                .filter(file -> file.getName().equals(name))
                .findFirst()
                .orElse(null);
        model.addAttribute("files", files);
        model.addAttribute("currentFile", currentFile);
        return "containers";
    }

    @PostMapping(value = "/{name}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void createFile(@PathVariable String name) {
        var fileEntity = FileEntity.builder()
                .id(name)
                .name(name)
                .content("")
                .build();
        fileRepository.saveFile(fileEntity);
    }

    @PutMapping(value = "/{name}", consumes = {MediaType.TEXT_PLAIN_VALUE})
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void saveFile(
            @PathVariable String name,
            @RequestBody String body
    ) {
        var fileEntity = FileEntity.builder()
                .id(name)
                .name(name)
                .content(body)
                .build();
        fileRepository.saveFile(fileEntity);
    }

    @DeleteMapping(value = "/{name}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void deleteFile(@PathVariable String name) {
        fileRepository.deleteFile(name);
    }
}
