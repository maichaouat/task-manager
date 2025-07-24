package org.tasksmanager.task.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tasksmanager.task.service.TaskService;
import org.tasksmanager.common.dto.TaskDTO;

import java.util.List;

@RestController
@RequestMapping("/api/projects/{projectId}/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public ResponseEntity<List<TaskDTO>> getAllTasks(
            @PathVariable Long projectId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        List<TaskDTO> tasks = taskService.getTasksByProjectId(projectId, page, size);
        return ResponseEntity.ok(tasks);
    }


    @GetMapping("/{taskId}")
    public ResponseEntity<TaskDTO> getTask(@PathVariable Long taskId) {
        return ResponseEntity.ok(taskService.getTask(taskId));
    }

    @PostMapping
    public ResponseEntity<TaskDTO> createTask(@PathVariable Long projectId,
                                              @RequestBody TaskDTO dto) {
        return ResponseEntity.ok(taskService.createTask(projectId, dto));
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<TaskDTO> updateTask(@PathVariable Long projectId,
                                              @PathVariable Long taskId,
                                              @RequestBody TaskDTO dto) {
        return ResponseEntity.ok(taskService.updateTask(projectId, taskId, dto));
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long taskId) {
        taskService.deleteTask(taskId);
        return ResponseEntity.noContent().build();
    }
}
