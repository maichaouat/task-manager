package org.tasksmanager.project.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "projects")
@Getter
@Setter
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;

    @Column(name = "user_id", nullable = false)
    private String userId;  // This will store the Cognito user ID (from the JWT "sub" claim)
}

