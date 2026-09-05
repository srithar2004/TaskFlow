package com.taskflow.taskflow.repository;

import com.taskflow.taskflow.entity.TaskComment;
import com.taskflow.taskflow.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TaskCommentRepository extends JpaRepository<TaskComment, Long> {
    List<TaskComment> findByTaskOrderByCreatedAtAsc(Task task);
}