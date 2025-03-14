package ru.renattele.admin95.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.renattele.admin95.model.UserEntity;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByNameEquals(String name);
}
