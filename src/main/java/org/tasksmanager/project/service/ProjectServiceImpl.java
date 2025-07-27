package org.tasksmanager.project.service;

import lombok.extern.slf4j.Slf4j;
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Slf4j
@Service
@Primary
public class ProjectServiceImpl implements ProjectService {


    private final ProjectRepository repository;
    private final ProjectMapper mapper;
    private final UserService userService;

    @Autowired
    public ProjectServiceImpl(ProjectRepository repository, ProjectMapper mapper, UserService userService) {
        this.repository = repository;
        this.mapper = mapper;
        this.userService = userService;
    }

    @Override
    public ProjectDTO create(ProjectDTO dto) {
        String userId = userService.getCurrentUserId();
        log.info("Creating project for userId={}", userId);
        Project project = mapper.toEntity(dto, userId);
        return mapper.toDto(repository.save(project));
    }

    @Override
    public ProjectDTO getById(Long id) {
        String userId = userService.getCurrentUserId();
        log.info("Fetching project id={} for userId={}", id, userId);

        return repository.findByIdAndUserId(id, userId)
                .map(mapper::toDto)
                .orElseThrow(() -> new RuntimeException("Project not found or access denied"));
    }

    @Override
    public List<ProjectDTO> getAll(int page, int size) {
        String userId = userService.getCurrentUserId();
        log.info("Fetching all projects for userId={} [page={}, size={}]", userId, page, size);

        Pageable pageable = PageRequest.of(page, size);
        return repository.findAllByUserId(userId, pageable)
                .map(mapper::toDto)
                .getContent();
    }

    @Override
    public ProjectDTO update(Long id, ProjectDTO dto) {
        String userId = userService.getCurrentUserId();
        log.info("Updating project id={} for userId={}", id, userId);

        Project entity = repository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new RuntimeException("Project not found or access denied"));

        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());

        return mapper.toDto(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        String userId = userService.getCurrentUserId();
        log.info("Deleting project id={} for userId={}", id, userId);

        Project entity = repository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new RuntimeException("Project not found or access denied"));

        repository.delete(entity);
    }
}
