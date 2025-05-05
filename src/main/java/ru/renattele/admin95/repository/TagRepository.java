package ru.renattele.admin95.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.renattele.admin95.model.TagEntity;

public interface TagRepository extends JpaRepository<TagEntity, Long> {
    TagEntity findTagEntityByName(String name);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM docker_project_tags WHERE tag_id = (SELECT id FROM tags WHERE id = :id); " +
            "DELETE FROM tags WHERE id = :id",
            nativeQuery = true)
    void deleteTagById(@Param("id") Long id);
}
