package ru.renattele.admin95.service;

import ru.renattele.admin95.model.docker.DockerProjectEntity;

import java.util.List;

public interface DockerProjectService {
   List<DockerProjectEntity> getProjects();

   boolean stopProject(DockerProjectEntity project);
   boolean startProject(DockerProjectEntity project);

   String logsForProject(DockerProjectEntity project);

   boolean createProject(String name);
   boolean removeProject(DockerProjectEntity project);
}
