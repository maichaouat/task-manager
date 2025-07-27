package org.tasksmanager.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Schema(description = "Data Transfer Object for a Project")
public class ProjectDTO {
    @Schema(description = "Unique identifier of the project", example = "1")
    private Long id;

    @Schema(description = "Name of the project", example = "Website Redesign")
    private String name;

    @Schema(description = "Detailed description of the project", example = "Revamp the company website for mobile responsiveness")
    private String description;

    // Constructors
    public ProjectDTO() {}

    public ProjectDTO(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }
}
