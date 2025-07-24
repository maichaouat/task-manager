package org.tasksmanager.project.service;

import org.tasksmanager.common.dto.ProjectDTO;
import java.util.List;

public interface ProjectService {
    ProjectDTO create(ProjectDTO dto);
    ProjectDTO getById(Long id);
    List<ProjectDTO> getAll(int page, int size);
    ProjectDTO update(Long id, ProjectDTO dto);
    void delete(Long id);
}
