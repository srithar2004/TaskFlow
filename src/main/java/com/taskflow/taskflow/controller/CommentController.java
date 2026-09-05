package com.taskflow.taskflow.controller;

import com.taskflow.taskflow.dto.CommentRequest;
import com.taskflow.taskflow.entity.TaskComment;
import com.taskflow.taskflow.entity.User;
import com.taskflow.taskflow.service.TaskCommentService;
import com.taskflow.taskflow.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks/{taskId}/comments")
public class CommentController {

    @Autowired
    private TaskCommentService commentService;

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<?> addComment(@PathVariable Long taskId, @RequestBody CommentRequest request,
                                          Authentication authentication) {
        User currentUser = userService.findByUsername(authentication.getName());
        TaskComment comment = commentService.addComment(taskId, request.getContent(), currentUser);
        return ResponseEntity.ok(comment);
    }

    @GetMapping
    public ResponseEntity<List<TaskComment>> getComments(@PathVariable Long taskId) {
        return ResponseEntity.ok(commentService.getCommentsForTask(taskId));
    }
}