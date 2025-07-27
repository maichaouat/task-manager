package org.tasksmanager.task.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.tasksmanager.project.entity.Project;
import org.tasksmanager.common.enums.TaskStatus;

@Getter
@Setter
@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    // Getters and Setters
}

