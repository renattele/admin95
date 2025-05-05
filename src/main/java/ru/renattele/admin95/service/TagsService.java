package ru.renattele.admin95.service;

import jakarta.annotation.Nullable;
import ru.renattele.admin95.dto.DockerProjectDto;
import ru.renattele.admin95.dto.TagDto;

import java.util.Set;

public interface TagsService {
    TagDto getTagByName(String name);
    @Nullable TagDto createTag(String tag);
    void deleteTag(TagDto tag);
    Set<TagDto> allTags();
    Set<DockerProjectDto> projectsFor(TagDto tag);
    Set<TagDto> tagsFor(DockerProjectDto project);

    Set<TagDto> suggestedTagsFor(DockerProjectDto project);

    void addTagToProject(TagDto tag, DockerProjectDto project);
    void removeTagFromProject(TagDto tag, DockerProjectDto project);
}
