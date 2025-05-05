package ru.renattele.admin95.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.renattele.admin95.api.TagsApi;
import ru.renattele.admin95.dto.DockerProjectDto;
import ru.renattele.admin95.dto.TagDto;
import ru.renattele.admin95.exception.ResourceNotFoundException;
import ru.renattele.admin95.service.TagsService;
import ru.renattele.admin95.service.docker.DockerProjectQueryService;

import java.util.Set;

@RestController
@RequiredArgsConstructor
public class TagsController implements TagsApi {
    private final TagsService tagsService;
    private final DockerProjectQueryService dockerProjectQueryService;

    @Override
    public Set<TagDto> allTags() {
        return tagsService.allTags();
    }

    @Override
    public Set<TagDto> tagsForProject(String projectName) {
        var project = dockerProjectQueryService.getProjectByName(projectName);
        if (project == null) throw new ResourceNotFoundException();

        return tagsService.tagsFor(project);
    }

    @Override
    public Set<DockerProjectDto> projectsForTag(String tagName) {
        var tag = tagsService.getTagByName(tagName);
        if (tag == null) throw new ResourceNotFoundException();

        return tagsService.projectsFor(tag);
    }

    @Override
    public void createTag(String name) {
        tagsService.createTag(name);
    }

    @Override
    public void deleteTag(String name) {
        var tag = tagsService.getTagByName(name);
        if (tag == null) throw new ResourceNotFoundException();

        tagsService.deleteTag(tag);
    }

    @Override
    public void addTagToProject(String projectName,
                                String name) {
        var tag = tagsService.getTagByName(name);
        if (tag == null) {
            tag = tagsService.createTag(name);
        }
        var project = dockerProjectQueryService.getProjectByName(projectName);
        if (tag == null || project == null) throw new ResourceNotFoundException();

        tagsService.addTagToProject(tag, project);
    }

    @Override
    public void removeTagFromProject(String projectName,
                                     String name) {
        var tag = tagsService.getTagByName(name);
        var project = dockerProjectQueryService.getProjectByName(projectName);
        if (tag == null || project == null) throw new ResourceNotFoundException();

        tagsService.removeTagFromProject(tag, project);
    }
}
