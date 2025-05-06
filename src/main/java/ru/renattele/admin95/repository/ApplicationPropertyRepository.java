package ru.renattele.admin95.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.renattele.admin95.model.ApplicationPropertyEntity;

public interface ApplicationPropertyRepository extends JpaRepository<ApplicationPropertyEntity, String> {
}
