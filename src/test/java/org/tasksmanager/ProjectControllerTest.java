package org.tasksmanager;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.tasksmanager.common.dto.ProjectDTO;
import org.tasksmanager.project.service.ProjectService;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")  // <<< ACTIVATE test profile to exclude main SecurityConfig
@ContextConfiguration(classes = {
        TaskmanagerApplication.class,
        TestSecurityConfig.class
})
public class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProjectService projectService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testHealthEndpoint() throws Exception {
        mockMvc.perform(get("/api/projects/health"))
                .andExpect(status().isOk())
                .andExpect(content().string("Server is running"));
    }

    @Test
    void testCreateProject() throws Exception {
        ProjectDTO request = new ProjectDTO(null, "Test Project", "Description");
        ProjectDTO response = new ProjectDTO(1L, "Test Project", "Description");

        Mockito.when(projectService.create(any(ProjectDTO.class))).thenReturn(response);

        mockMvc.perform(post("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Test Project"))
                .andExpect(jsonPath("$.description").value("Description"));
    }

    @Test
    void testGetProjectById() throws Exception {
        ProjectDTO dto = new ProjectDTO(1L, "Get Project", "Get Description");

        Mockito.when(projectService.getById(1L)).thenReturn(dto);

        mockMvc.perform(get("/api/projects/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void testUpdateProject() throws Exception {
        ProjectDTO request = new ProjectDTO(null, "Updated Project", "Updated Description");
        ProjectDTO updated = new ProjectDTO(1L, "Updated Project", "Updated Description");

        Mockito.when(projectService.update(eq(1L), any(ProjectDTO.class))).thenReturn(updated);

        mockMvc.perform(put("/api/projects/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Updated Project"));
    }

    @Test
    void testDeleteProject() throws Exception {
        mockMvc.perform(delete("/api/projects/1"))
                .andExpect(status().isNoContent());

        Mockito.verify(projectService).delete(1L);
    }

    @Test
    void testGetAllProjects() throws Exception {
        List<ProjectDTO> projects = Arrays.asList(
                new ProjectDTO(1L, "Project 1", "Desc 1"),
                new ProjectDTO(2L, "Project 2", "Desc 2")
        );

        Mockito.when(projectService.getAll(0, 10)).thenReturn(projects);

        mockMvc.perform(get("/api/projects"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }
}
