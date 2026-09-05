package com.taskflow.taskflow.service;

import com.taskflow.taskflow.dto.TaskRequest;
import com.taskflow.taskflow.dto.TaskResponse;
import com.taskflow.taskflow.entity.Task;
import com.taskflow.taskflow.entity.TaskStatus;
import com.taskflow.taskflow.entity.User;
import com.taskflow.taskflow.repository.TaskRepository;
import com.taskflow.taskflow.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    public TaskResponse createTask(TaskRequest request, User createdBy) {
        User assignedTo = userRepository.findById(request.getAssignedToId())
                .orElseThrow(() -> new RuntimeException("Assigned user not found"));

        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setPriority(request.getPriority());
        task.setDueDate(request.getDueDate());
        task.setStatus(TaskStatus.TODO);
        task.setAssignedTo(assignedTo);
        task.setCreatedBy(createdBy);

        Task saved = taskRepository.save(task);
        return toResponse(saved);
    }

    public List<TaskResponse> getTasksForUser(User user) {
        return taskRepository.findByAssignedTo(user)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<TaskResponse> getAllTasks() {
        return taskRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public TaskResponse updateStatus(Long taskId, TaskStatus newStatus, User requester) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        boolean isOwner = task.getAssignedTo().getId().equals(requester.getId());
        boolean isAdmin = requester.getRole().name().equals("ADMIN");

        if (!isOwner && !isAdmin) {
            throw new RuntimeException("Not authorized to update this task");
        }

        task.setStatus(newStatus);
        Task updated = taskRepository.save(task);
        return toResponse(updated);
    }

    public long countByStatus(TaskStatus status) {
        return taskRepository.countByStatus(status);
    }

    private TaskResponse toResponse(Task task) {
        TaskResponse dto = new TaskResponse();
        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setStatus(task.getStatus());
        dto.setPriority(task.getPriority());
        dto.setDueDate(task.getDueDate());
        dto.setCreatedAt(task.getCreatedAt());
        dto.setUpdatedAt(task.getUpdatedAt());
        dto.setAssignedToUsername(task.getAssignedTo().getUsername());
        dto.setCreatedByUsername(task.getCreatedBy().getUsername());
        return dto;
    }
}