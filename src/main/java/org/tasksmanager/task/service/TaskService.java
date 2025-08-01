package org.tasksmanager.task.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.tasksmanager.task.repository.TaskRepository;
import org.tasksmanager.task.mapper.TaskMapper;
import org.tasksmanager.project.repository.ProjectRepository;
import org.tasksmanager.task.entity.Task;
import org.tasksmanager.project.entity.Project;
import org.tasksmanager.common.dto.TaskDTO;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final TaskMapper taskMapper;

    @Autowired
    public TaskService(TaskRepository taskRepository,
                       ProjectRepository projectRepository,
                       TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
        this.taskMapper = taskMapper;
    }

    public List<TaskDTO> getTasksByProjectId(Long projectId, int page, int size) {
        log.info("Getting tasks for projectId={}", projectId);
        Pageable pageable = PageRequest.of(page, size);
        return taskRepository.findByProjectId(projectId, pageable).stream()
                .map(taskMapper::toDTO)
                .collect(Collectors.toList());
    }


    public TaskDTO getTask(Long taskId) {
        log.info("Getting task for taskId={}", taskId);
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        return taskMapper.toDTO(task);
    }

    public TaskDTO createTask(Long projectId, TaskDTO dto) {
        log.info("Creating task for projectId={}", projectId);
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        Task task = taskMapper.toEntity(dto);
        task.setProject(project);
        taskRepository.save(task);

        return taskMapper.toDTO(task);
    }

    public TaskDTO updateTask(Long projectId, Long taskId, TaskDTO dto) {
        log.info("Updating task for projectId={}, taskId={}", projectId, taskId);
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setStatus(dto.getStatus());

        taskRepository.save(task);
        return taskMapper.toDTO(task);
    }

    public void deleteTask(Long taskId) {
        log.info("Deleting task for taskId={}", taskId);
        taskRepository.deleteById(taskId);
    }
}