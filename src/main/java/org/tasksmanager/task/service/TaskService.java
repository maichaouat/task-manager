package org.tasksmanager.task.service;

import org.springframework.stereotype.Service;
import org.tasksmanager.task.repository.TaskRepository;
import org.tasksmanager.task.mapper.TaskMapper;
import org.tasksmanager.project.repository.ProjectRepository;
import org.tasksmanager.task.entity.Task;
import org.tasksmanager.project.entity.Project;
import org.tasksmanager.common.dto.TaskDTO;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final TaskMapper taskMapper;

    public TaskService(TaskRepository taskRepository,
                       ProjectRepository projectRepository,
                       TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
        this.taskMapper = taskMapper;
    }

    public List<TaskDTO> getTasksByProjectId(Long projectId) {
        return taskRepository.findByProjectId(projectId).stream()
                .map(taskMapper::toDTO)
                .collect(Collectors.toList());
    }

    public TaskDTO getTask(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        return taskMapper.toDTO(task);
    }

    public TaskDTO createTask(Long projectId, TaskDTO dto) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        Task task = taskMapper.toEntity(dto);
        task.setProject(project);
        taskRepository.save(task);

        return taskMapper.toDTO(task);
    }

    public TaskDTO updateTask(Long projectId, Long taskId, TaskDTO dto) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setStatus(dto.getStatus());

        taskRepository.save(task);
        return taskMapper.toDTO(task);
    }

    public void deleteTask(Long taskId) {
        taskRepository.deleteById(taskId);
    }
}