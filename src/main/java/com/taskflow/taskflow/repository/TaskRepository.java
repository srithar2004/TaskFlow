package com.taskflow.taskflow.repository;

import com.taskflow.taskflow.entity.Task;
import com.taskflow.taskflow.entity.TaskStatus;
import com.taskflow.taskflow.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByAssignedTo(User user);
    List<Task> findByAssignedToAndStatus(User user, TaskStatus status);
    List<Task> findByStatus(TaskStatus status);
    long countByStatus(TaskStatus status);
    long countByAssignedToAndStatus(User user, TaskStatus status);
}