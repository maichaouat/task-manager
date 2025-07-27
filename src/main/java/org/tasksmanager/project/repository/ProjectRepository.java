package org.tasksmanager.project.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.tasksmanager.project.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    Page<Project> findAllByUserId(String userId, Pageable pageable);

    Optional<Project> findByIdAndUserId(Long id, String userId);
}