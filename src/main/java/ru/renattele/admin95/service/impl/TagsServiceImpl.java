package ru.renattele.admin95.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.renattele.admin95.dto.DockerProjectDto;
import ru.renattele.admin95.dto.TagDto;
import ru.renattele.admin95.exception.ResourceNotFoundException;
import ru.renattele.admin95.mapper.DockerProjectDetailsMapper;
import ru.renattele.admin95.mapper.DockerProjectMapper;
import ru.renattele.admin95.mapper.TagMapper;
import ru.renattele.admin95.model.TagEntity;
import ru.renattele.admin95.repository.TagRepository;
import ru.renattele.admin95.service.TagsService;
import ru.renattele.admin95.service.docker.DockerProjectManagementService;
import ru.renattele.admin95.service.docker.DockerProjectQueryService;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class TagsServiceImpl implements TagsService {
    private final TagRepository tagRepository;
    private final TagMapper tagMapper;
    private final DockerProjectMapper dockerProjectMapper;
    private final DockerProjectQueryService dockerProjectQueryService;
    private final DockerProjectManagementService dockerProjectManagementService;

    @Override
    public TagDto getTagByName(String name) {
        var entity = tagRepository.findTagEntityByName(name);
        if (entity == null) {
            return null;
        }
        return tagMapper.toDto(entity);
    }

    @Override
    public TagDto createTag(String tagName) {
        try {
            var tagEntity = TagEntity.builder()
                    .name(tagName)
                    .dockerProjects(new HashSet<>())
                    .build();
            return tagMapper.toDto(tagRepository.save(tagEntity));
        } catch (Exception e) {
            log.error("Failed to create tag: {}", tagName, e);
            return null;
        }
    }

    @Override
    public void deleteTag(TagDto tag) {
        var entity = tagMapper.toEntity(tag);
        if (entity == null || entity.getId() == null) {
            throw new ResourceNotFoundException("Tag not found");
        }
        tagRepository.deleteTagById(entity.getId());
    }

    @Override
    public Set<TagDto> allTags() {
        return tagRepository.findAll().stream()
                .map(tagMapper::toDto)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<DockerProjectDto> projectsFor(TagDto tag) {
        var entity = tagMapper.toEntity(tag);
        if (entity == null) {
            throw new ResourceNotFoundException("Tag not found");
        }
        return entity.getDockerProjects().stream()
                .map(dockerProjectMapper::toDto)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<TagDto> tagsFor(DockerProjectDto project) {
        var details = dockerProjectQueryService.getProjectDetails(project);
        var entity = dockerProjectMapper.toEntity(project, details);
        if (entity == null) {
            throw new ResourceNotFoundException("Project not found");
        }
        return entity.getTags().stream()
                .map(tagMapper::toDto)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<TagDto> suggestedTagsFor(DockerProjectDto project) {
        var allTags = allTags();
        allTags.removeAll(tagsFor(project));
        return allTags;
    }

    @Override
    public void addTagToProject(TagDto tag, DockerProjectDto project) {
        var details = dockerProjectQueryService.getProjectDetails(project);
        var tagEntity = tagMapper.toEntity(tag);
        var projectEntity = dockerProjectMapper.toEntity(project, details);
        if (tagEntity == null || projectEntity == null) {
            throw new ResourceNotFoundException("Tag or project not found");
        }
        var tagEntityFromDb = tagRepository.findTagEntityByName(tag.getName());
        tagEntity.setDockerProjects(tagEntityFromDb.getDockerProjects());
        project.getTags().add(tag);
        tagEntity.getDockerProjects().add(projectEntity);
        dockerProjectManagementService.updateProject(project);
        tagRepository.save(tagEntity);
    }

    @Override
    public void removeTagFromProject(TagDto tag, DockerProjectDto project) {
        var details = dockerProjectQueryService.getProjectDetails(project);
        var tagEntity = tagMapper.toEntity(tag);
        var projectEntity = dockerProjectMapper.toEntity(project, details);
        if (tagEntity == null || projectEntity == null) {
            throw new ResourceNotFoundException("Tag or project not found");
        }
        var tagEntityFromDb = tagRepository.findTagEntityByName(tag.getName());
        tagEntity.setDockerProjects(tagEntityFromDb.getDockerProjects());

        project.getTags().remove(tag);
        dockerProjectManagementService.updateProject(project);
        tagEntity.getDockerProjects().remove(projectEntity);
        tagRepository.save(tagEntity);
    }
}
