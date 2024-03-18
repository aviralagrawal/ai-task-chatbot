package com.project.aichatbot.controller;

import com.project.aichatbot.dto.TaskDTO;
import com.project.aichatbot.entity.Task;
import com.project.aichatbot.entity.User;
import com.project.aichatbot.service.TaskService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TaskControllerTest {

    @InjectMocks
    private TaskController taskController;

    @Mock
    private TaskService taskService;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllTasks() {
        // Mock task service
        List<Task> tasks = new ArrayList<>();
        when(taskService.getAllTasks()).thenReturn(tasks);

        // Invoke controller method
        ResponseEntity<List<Task>> responseEntity = taskController.getAllTasks();

        // Verify the response
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(tasks, responseEntity.getBody());
    }

    @Test
    public void testCreateTask() {
        // Prepare sample TaskDTO
        TaskDTO taskDTO = new TaskDTO();

        // Mock task service
        Mockito.doNothing().when(taskService).createTask(taskDTO);

        // Invoke controller method
        ResponseEntity<String> responseEntity = taskController.createTask(taskDTO);

        // Verify the response
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Task created successfully", responseEntity.getBody());
    }

    @Test
    public void testEditTask() {
        // Prepare sample TaskDTO and task ID
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setName("Updated Task Name");
        Long taskId = 1L;

        // Mock authentication
        Jwt jwt = Mockito.mock(Jwt.class);
        Mockito.when(jwt.getSubject()).thenReturn("user@example.com");
        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getPrincipal()).thenReturn(jwt);

        // Mock task retrieval
        Task existingTask = new Task();
        existingTask.setId(taskId);
        existingTask.setName("Original Task Name");
        User user = new User();
        user.setEmail("user@example.com");
        existingTask.setUser(user);
        Mockito.when(taskService.getTaskbyId(taskId)).thenReturn(existingTask);

        // Invoke controller method
        ResponseEntity<String> responseEntity = taskController.editTask(taskId, taskDTO, authentication);

        // Verify the response
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Task edited successfully", responseEntity.getBody());

        // Verify that the task service's editTask method was called with the correct arguments
        Mockito.verify(taskService).editTask(eq(taskId), eq(taskDTO));
    }

    @Test
    public void testDeleteTask() {
        // Prepare sample task ID
        Long taskId = 1L;

        // Mock authentication
        Jwt jwt = Mockito.mock(Jwt.class);
        Mockito.when(jwt.getSubject()).thenReturn("user@example.com");
        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getPrincipal()).thenReturn(jwt);

        // Mock task retrieval
        Task existingTask = new Task();
        existingTask.setId(taskId);
        User user = new User();
        user.setEmail("user@example.com");
        existingTask.setUser(user);
        Mockito.when(taskService.getTaskbyId(taskId)).thenReturn(existingTask);

        // Invoke controller method
        ResponseEntity<String> responseEntity = taskController.deleteTask(taskId, authentication);

        // Verify the response
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Task deleted successfully", responseEntity.getBody());

        // Verify that the task service's deleteTask method was called with the correct argument
        Mockito.verify(taskService).deleteTask(eq(taskId));
    }

    @Test
    public void testEditTask_UnauthorizedUser() {
        // Prepare sample TaskDTO and task ID
        TaskDTO taskDTO = new TaskDTO();
        Long taskId = 1L;

        // Mock authentication
        Jwt jwt = Mockito.mock(Jwt.class);
        Mockito.when(jwt.getSubject()).thenReturn("otheruser@example.com");
        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getPrincipal()).thenReturn(jwt);

        // Mock task retrieval
        Task existingTask = new Task();
        existingTask.setId(taskId);
        User user = new User();
        user.setEmail("user@example.com");
        existingTask.setUser(user);
        Mockito.when(taskService.getTaskbyId(taskId)).thenReturn(existingTask);

        // Invoke controller method
        ResponseEntity<String> responseEntity = taskController.editTask(taskId, taskDTO, authentication);

        // Verify the response
        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
        assertEquals("You are not authorized to edit this task.", responseEntity.getBody());

        // Verify that the task service's editTask method was not called
        Mockito.verify(taskService, Mockito.never()).editTask(eq(taskId), eq(taskDTO));
    }

    @Test
    public void testDeleteTask_UnauthorizedUser() {
        // Prepare sample task ID
        Long taskId = 1L;

        // Mock authentication
        Jwt jwt = Mockito.mock(Jwt.class);
        Mockito.when(jwt.getSubject()).thenReturn("otheruser@example.com");
        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getPrincipal()).thenReturn(jwt);

        // Mock task retrieval
        Task existingTask = new Task();
        existingTask.setId(taskId);
        User user = new User();
        user.setEmail("user@example.com");
        existingTask.setUser(user);
        Mockito.when(taskService.getTaskbyId(taskId)).thenReturn(existingTask);

        // Invoke controller method
        ResponseEntity<String> responseEntity = taskController.deleteTask(taskId, authentication);

        // Verify the response
        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
        assertEquals("You are not authorized to delete this task.", responseEntity.getBody());

        // Verify that the task service's deleteTask method was not called
        Mockito.verify(taskService, Mockito.never()).deleteTask(eq(taskId));
    }
}

