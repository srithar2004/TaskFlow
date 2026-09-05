package com.taskflow.taskflow.controller;

import com.taskflow.taskflow.dto.TaskRequest;
import com.taskflow.taskflow.dto.TaskResponse;
import com.taskflow.taskflow.entity.TaskStatus;
import com.taskflow.taskflow.entity.User;
import com.taskflow.taskflow.service.TaskService;
import com.taskflow.taskflow.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<?> createTask(@RequestBody TaskRequest request, Authentication authentication) {
        User currentUser = userService.findByUsername(authentication.getName());

        if (!currentUser.getRole().name().equals("ADMIN")) {
            return ResponseEntity.status(403).body("Only admins can create tasks");
        }

        TaskResponse response = taskService.createTask(request, currentUser);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/my-tasks")
    public ResponseEntity<List<TaskResponse>> getMyTasks(Authentication authentication) {
        User currentUser = userService.findByUsername(authentication.getName());
        return ResponseEntity.ok(taskService.getTasksForUser(currentUser));
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllTasks(Authentication authentication) {
        User currentUser = userService.findByUsername(authentication.getName());

        if (!currentUser.getRole().name().equals("ADMIN")) {
            return ResponseEntity.status(403).body("Only admins can view all tasks");
        }

        return ResponseEntity.ok(taskService.getAllTasks());
    }

    @PatchMapping("/{taskId}/status")
    public ResponseEntity<?> updateStatus(@PathVariable Long taskId, @RequestParam TaskStatus status,
                                            Authentication authentication) {
        User currentUser = userService.findByUsername(authentication.getName());
        TaskResponse response = taskService.updateStatus(taskId, status, currentUser);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/dashboard")
    public ResponseEntity<?> getDashboard() {
        long todo = taskService.countByStatus(TaskStatus.TODO);
        long inProgress = taskService.countByStatus(TaskStatus.IN_PROGRESS);
        long done = taskService.countByStatus(TaskStatus.DONE);

        return ResponseEntity.ok(new Object() {
            public final long TODO = todo;
            public final long IN_PROGRESS = inProgress;
            public final long DONE = done;
        });
    }
}