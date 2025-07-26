package org.tasksmanager.common.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.tasksmanager.common.enums.TaskStatus;

@Getter @Setter
@Schema(description = "Task DTO inside a project")
public class TaskDTO {
    private Long id;

    @Schema(description = "Task title", example = "Design homepage")
    private String title;

    @Schema(description = "Task description", example = "Create new layout for the homepage")
    private String description;

    @Schema(description = "Task status", example = "IN_PROGRESS")
    private TaskStatus status;

    // Constructors
    public TaskDTO() {}

    public TaskDTO(Long id, String title, String description, TaskStatus status) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
    }
}
