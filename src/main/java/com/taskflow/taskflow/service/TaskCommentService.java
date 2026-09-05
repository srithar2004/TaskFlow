package com.taskflow.taskflow.service;

import com.taskflow.taskflow.entity.Task;
import com.taskflow.taskflow.entity.TaskComment;
import com.taskflow.taskflow.entity.User;
import com.taskflow.taskflow.repository.TaskCommentRepository;
import com.taskflow.taskflow.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskCommentService {

    @Autowired
    private TaskCommentRepository commentRepository;

    @Autowired
    private TaskRepository taskRepository;

    public TaskComment addComment(Long taskId, String content, User commentedBy) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        TaskComment comment = new TaskComment();
        comment.setContent(content);
        comment.setTask(task);
        comment.setCommentedBy(commentedBy);

        return commentRepository.save(comment);
    }

    public List<TaskComment> getCommentsForTask(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        return commentRepository.findByTaskOrderByCreatedAtAsc(task);
    }
}