package org.tasksmanager;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.tasksmanager.common.dto.TaskDTO;
import org.tasksmanager.common.enums.TaskStatus;
import org.tasksmanager.task.controller.TaskController;
import org.tasksmanager.task.service.TaskService;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
@ContextConfiguration(classes = TaskController.class)
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TaskService taskService;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    void testGetAllTasksWithPagination() throws Exception {
        Long projectId = 1L;
        int page = 0;
        int size = 2;

        List<TaskDTO> mockTasks = List.of(
                new TaskDTO(1L, "Task 1", "Description 1", TaskStatus.TODO),
                new TaskDTO(2L, "Task 2", "Description 2", TaskStatus.IN_PROGRESS)
        );

        when(taskService.getTasksByProjectId(projectId, page, size)).thenReturn(mockTasks);

        mockMvc.perform(get("/api/projects/{projectId}/tasks", projectId)
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].title").value("Task 1"))
                .andExpect(jsonPath("$[1].status").value("IN_PROGRESS"));
    }


    @Test
    void testCreateTask() throws Exception {
        Long projectId = 1L;

        TaskDTO request = new TaskDTO(null, "New Task", "Do something", TaskStatus.IN_PROGRESS);
        TaskDTO response = new TaskDTO(10L, "New Task", "Do something", TaskStatus.IN_PROGRESS);

        when(taskService.createTask(eq(projectId), any(TaskDTO.class))).thenReturn(response);

        mockMvc.perform(post("/api/projects/{projectId}/tasks", projectId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"));
    }

    @Test
    void testDeleteTask() throws Exception {
        Long taskId = 42L;

        mockMvc.perform(delete("/api/projects/{projectId}/tasks/{taskId}", 1L, taskId))
                .andExpect(status().isNoContent());

        verify(taskService, times(1)).deleteTask(taskId);
    }
}
