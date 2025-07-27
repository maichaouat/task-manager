package org.tasksmanager.project.mapper;

import org.tasksmanager.project.entity.Project;
import org.tasksmanager.common.dto.ProjectDTO;
import org.springframework.stereotype.Component;

@Component
public class ProjectMapper {

    public Project toEntity(ProjectDTO dto, String userId) {
        Project project = new Project();
        project.setName(dto.getName());
        project.setDescription(dto.getDescription());
        project.setUserId(userId);
        return project;
    }

    public ProjectDTO toDto(Project entity) {
        return new ProjectDTO(entity.getId(), entity.getName(), entity.getDescription());
    }
}
