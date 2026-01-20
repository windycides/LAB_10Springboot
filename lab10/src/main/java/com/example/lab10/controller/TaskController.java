package com.example.lab10.controller;

import com.example.lab10.dto.TaskDto;
import com.example.lab10.model.Task;
import com.example.lab10.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public List<Task> getMyTasks(Authentication authentication) {
        return taskService.getTasksForUser(authentication.getName());
    }

    @PostMapping
    public ResponseEntity<Task> createTask(@Valid @RequestBody TaskDto taskDto, Authentication authentication) {
        Task createdTask = taskService.createTask(taskDto, authentication.getName());
        return ResponseEntity.ok(createdTask);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteTask(@PathVariable Long id, Authentication authentication) {
        try {
            // delete the task
            taskService.deleteTask(id, authentication.getName());

            // If successful, return 200 OK
            return ResponseEntity.ok(Map.of("message", "Task deleted successfully"));

        } catch (RuntimeException e) {
            // If it fails (Access Denied), return 403 Forbidden with the error message
            return ResponseEntity.status(403).body(Map.of("error", e.getMessage()));
        }
    }
}