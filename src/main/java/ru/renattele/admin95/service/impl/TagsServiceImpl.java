package ru.renattele.admin95.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.renattele.admin95.dto.DockerProjectDto;
import ru.renattele.admin95.dto.TagDto;
import ru.renattele.admin95.exception.ResourceNotFoundException;
import ru.renattele.admin95.mapper.DockerProjectMapper;
import ru.renattele.admin95.mapper.TagMapper;
import ru.renattele.admin95.model.TagEntity;
import ru.renattele.admin95.model.docker.DockerProjectEntity;
import ru.renattele.admin95.repository.TagRepository;
import ru.renattele.admin95.service.TagsService;
import ru.renattele.admin95.service.docker.DockerProjectManagementService;
import ru.renattele.admin95.service.docker.DockerProjectQueryService;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
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
        return findTagEntityByName(name)
                .map(tagMapper::toDto)
                .orElse(null);
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
        if (tag == null || tag.getId() == null) {
            throw new ResourceNotFoundException("Tag not found");
        }
        tagRepository.deleteTagById(tag.getId());
    }

    @Override
    public Set<TagDto> allTags() {
        return tagRepository.findAll().stream()
                .map(tagMapper::toDto)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<DockerProjectDto> projectsFor(TagDto tag) {
        return findTagEntityByName(tag.getName())
                .map(entity -> mapProjectsToDto(entity.getDockerProjects()))
                .orElseThrow(() -> new ResourceNotFoundException("Tag not found"));
    }

    @Override
    public Set<TagDto> tagsFor(DockerProjectDto project) {
        var projectEntity = getProjectEntity(project);
        return projectEntity.getTags().stream()
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
        var tagEntity = getExistingTagEntity(tag);
        var projectEntity = getProjectEntity(project);
        
        // Update the project
        project.getTags().add(tag);
        dockerProjectManagementService.updateProject(project);
        
        // Update the tag
        tagEntity.getDockerProjects().add(projectEntity);
        tagRepository.save(tagEntity);
    }

    @Override
    public void removeTagFromProject(TagDto tag, DockerProjectDto project) {
        var tagEntity = getExistingTagEntity(tag);
        var projectEntity = getProjectEntity(project);
        
        // Update the project
        project.getTags().remove(tag);
        dockerProjectManagementService.updateProject(project);
        
        // Update the tag
        tagEntity.getDockerProjects().remove(projectEntity);
        tagRepository.save(tagEntity);
    }
    
    // Helper methods
    
    /**
     * Finds a tag entity by name and returns it wrapped in an Optional
     */
    private Optional<TagEntity> findTagEntityByName(String name) {
        return Optional.ofNullable(tagRepository.findTagEntityByName(name));
    }
    
    /**
     * Gets an existing tag entity or throws an exception if not found
     */
    private TagEntity getExistingTagEntity(TagDto tag) {
        var tagEntity = tagMapper.toEntity(tag);
        if (tagEntity == null) {
            throw new ResourceNotFoundException("Tag not found");
        }
        
        var tagEntityFromDb = findTagEntityByName(tag.getName())
                .orElseThrow(() -> new ResourceNotFoundException("Tag not found"));
                
        tagEntity.setDockerProjects(tagEntityFromDb.getDockerProjects());
        return tagEntity;
    }
    
    /**
     * Gets a project entity from a project DTO
     */
    private DockerProjectEntity getProjectEntity(DockerProjectDto project) {
        var details = dockerProjectQueryService.getProjectDetails(project);
        var projectEntity = dockerProjectMapper.toEntity(project, details);
        if (projectEntity == null) {
            throw new ResourceNotFoundException("Project not found");
        }
        return projectEntity;
    }
    
    /**
     * Maps a set of project entities to DTOs
     */
    private Set<DockerProjectDto> mapProjectsToDto(Set<DockerProjectEntity> projects) {
        if (projects == null) {
            return Collections.emptySet();
        }
        return projects.stream()
                .map(dockerProjectMapper::toDto)
                .collect(Collectors.toSet());
    }
}
