package org.tasksmanager.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.tasksmanager.auth.service.UserService;
import org.tasksmanager.common.dto.ProjectDTO;
import org.tasksmanager.project.entity.Project;
import org.tasksmanager.project.repository.ProjectRepository;
import org.tasksmanager.project.mapper.ProjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.*;

import java.util.List;

@Service
@Primary
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository repository;
    private final ProjectMapper mapper;
    private final UserService userService;

    @Autowired
    public ProjectServiceImpl(ProjectRepository repository, ProjectMapper mapper,UserService userService) {
        this.repository = repository;
        this.mapper = mapper;
        this.userService = userService;
    }

    @Override
    public ProjectDTO create(ProjectDTO dto) {
        // Inject user ID from Cognito
        String userId = userService.getCurrentUserId();
        Project project = mapper.toEntity(dto, userId);
        return mapper.toDto(repository.save(project));
    }

    @Override
    public ProjectDTO getById(Long id) {
        return repository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new RuntimeException("Project not found"));
    }

    @Override
    public List<ProjectDTO> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findAll(pageable)
                .map(mapper::toDto)
                .getContent();
    }

    @Override
    public ProjectDTO update(Long id, ProjectDTO dto) {
        Project entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        return mapper.toDto(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
