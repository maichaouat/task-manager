package org.tasksmanager.task.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.tasksmanager.task.entity.Task;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByProjectId(Long projectId);
}
