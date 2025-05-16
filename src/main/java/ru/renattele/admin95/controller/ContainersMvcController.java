package ru.renattele.admin95.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import ru.renattele.admin95.api.ContainersMvcApi;
import ru.renattele.admin95.exception.ConflictException;
import ru.renattele.admin95.exception.ResourceNotFoundException;
import ru.renattele.admin95.model.FileEntity;
import ru.renattele.admin95.repository.FileRepository;
import ru.renattele.admin95.service.TagsService;
import ru.renattele.admin95.service.docker.DockerLifecycleService;
import ru.renattele.admin95.service.docker.DockerProjectManagementService;
import ru.renattele.admin95.service.docker.DockerProjectQueryService;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ContainersMvcController implements ContainersMvcApi {
    private final FileRepository fileRepository;
    private final DockerLifecycleService dockerLifecycleService;
    private final DockerProjectManagementService dockerProjectManagementService;
    private final DockerProjectQueryService dockerProjectQueryService;
    private final TagsService tagsService;

    @Override
    public String containers(
            String name,
            Model model
    ) {
        var allFiles = fileRepository.getFiles();
        var currentFile = allFiles.stream()
                .filter(file -> file.getName().equals(name))
                .findFirst()
                .orElseGet(() -> {
                    var dockerProject = dockerProjectQueryService.getProjectByName(name);
                    if (dockerProject == null) {
                        return null;
                    }
                    var details = dockerProjectQueryService.getProjectDetails(dockerProject);
                    return FileEntity.builder()
                            .name(dockerProject.getName())
                            .content(details.getContent())
                            .build();
                });
        var dockerProjects = dockerProjectQueryService.getProjects();
        var currentDockerProject = dockerProjectQueryService.getProjectByName(name);
        var projectLogs = currentDockerProject != null ?
                dockerProjectQueryService.logsForProject(currentDockerProject) :
                null;
        var tags = currentDockerProject != null ?
                tagsService.suggestedTagsFor(currentDockerProject) :
                null;
        model.addAttribute("files", allFiles);
        model.addAttribute("dockerProjects", dockerProjects);
        model.addAttribute("currentFile", currentFile);
        model.addAttribute("currentDockerProject", currentDockerProject);
        model.addAttribute("currentDockerProjectLogs", projectLogs);
        model.addAttribute("tags", tags);
        return "containers";
    }

    @Override
    public void createProject(String name) {
        dockerProjectManagementService.createProject(name);
    }

    @Override
    public void createFile(String name) {
        var dockerProject = dockerProjectQueryService.getProjectByName(name);
        if (dockerProject == null) {
            fileRepository.saveFile(FileEntity.builder().name(name).build());
        } else throw new ConflictException();
    }

    @Override
    public void startProject(String name) {
        var project = dockerProjectQueryService.getProjectByName(name);
        if (project == null) throw new ResourceNotFoundException();
        dockerLifecycleService.startProject(project);
    }

    @Override
    public void stopProject(String name) {
        var project = dockerProjectQueryService.getProjectByName(name);
        if (project == null) throw new ResourceNotFoundException();
        dockerLifecycleService.stopProject(project);
    }

    @Override
    public ResponseEntity<String> saveFile(
            String name,
            SaveFileRequest rawBody
    ) {
        var body = rawBody != null ? rawBody : new SaveFileRequest("", "");
        var project = dockerProjectQueryService.getProjectByName(name);
        if (project == null) {
            var file = fileRepository.getFile(name);
            if (file != null) {
                file.setContent(body.content());
                fileRepository.saveFile(file);
                return ResponseEntity.ok().build();
            }
            return ResponseEntity.notFound().build();
        }
        var projectDetails = dockerProjectQueryService.getProjectDetails(project);
        projectDetails.setContent(body.content());
        project.setLink(body.link());
        dockerProjectManagementService.updateProject(project);
        dockerProjectManagementService.updateProjectDetails(projectDetails);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<String> deleteFile(String name) {
        if (!fileRepository.deleteFile(name)) {
            var project = dockerProjectQueryService.getProjectByName(name);
            if (project == null) {
                return ResponseEntity.notFound().build();
            }
            dockerProjectManagementService.removeProject(project);
        }
        return ResponseEntity.ok().build();
    }
}
