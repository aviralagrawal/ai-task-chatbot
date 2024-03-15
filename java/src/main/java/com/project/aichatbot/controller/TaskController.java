package com.project.aichatbot.controller;

import com.project.aichatbot.dto.TaskDTO;
import com.project.aichatbot.entity.Task;
import com.project.aichatbot.entity.User;
import com.project.aichatbot.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    @Autowired
    private TaskService taskService;

    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {

        List<Task> tasks = taskService.getAllTasks();
        return ResponseEntity.ok(tasks);
    }

    @PostMapping
    public ResponseEntity<String> createTask(@RequestBody TaskDTO taskDTO) {

        taskService.createTask(taskDTO);
        return ResponseEntity.ok("Task created successfully");
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<String> editTask(@PathVariable Long taskId, @RequestBody TaskDTO taskDTO, Authentication authentication) {

        // Parse the JWT token
        Jwt jwt = (Jwt) authentication.getPrincipal();

        // Extract the user ID from the JWT claims
        String userEmail = jwt.getSubject();

        // Check if the task exists
        Task existingTask = taskService.getTaskbyId(taskId);

        if (existingTask == null) {
            return ResponseEntity.notFound().build();
        }

        // Check if the authenticated user is the owner of the task
        if (!existingTask.getUser().getEmail().equals(userEmail)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to edit this task.");
        }

        // Proceed with editing the task
        taskService.editTask(taskId, taskDTO);
        return ResponseEntity.ok("Task edited successfully");
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<String> deleteTask(@PathVariable Long taskId, Authentication authentication) {

        // Parse the JWT token
        Jwt jwt = (Jwt) authentication.getPrincipal();

        // Extract the user ID from the JWT claims
        String userEmail = jwt.getSubject();

        // Check if the task exists
        Task existingTask = taskService.getTaskbyId(taskId);
        if (existingTask == null) {
            return ResponseEntity.notFound().build();
        }

        // Check if the authenticated user is the owner of the task
        if (!existingTask.getUser().getEmail().equals(userEmail)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to delete this task.");
        }

        // Proceed with deleting the task
        taskService.deleteTask(taskId);
        return ResponseEntity.ok("Task deleted successfully");
    }
}
