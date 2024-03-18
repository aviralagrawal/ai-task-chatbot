package com.project.aichatbot.service;

import com.project.aichatbot.dto.TaskDTO;
import com.project.aichatbot.entity.Task;
import com.project.aichatbot.entity.User;
import com.project.aichatbot.exception.TaskNotFoundException;
import com.project.aichatbot.repository.TaskRepository;
import com.project.aichatbot.repository.UserRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TaskServiceImpl taskService;

    private User user;
    private Task task;

    @Before
    public void setUp() {
        // Mock User
        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setPassword("password123");

        // Mock Task
        task = new Task();
        task.setId(1L);
        task.setName("Test Task");
        task.setDueDate(LocalDateTime.now().plusDays(1));
        task.setPriority(1);
        task.setUser(user);
    }

    @Test
    public void testGetAllTasks() {
        // Mock data
        List<Task> tasks = new ArrayList<>();
        tasks.add(task);
        // Setup mock behavior
        Mockito.when(taskRepository.findAll()).thenReturn(tasks);
        // Perform the test
        List<Task> result = taskService.getAllTasks();
        // Assertions
        Assert.assertEquals(tasks.size(), result.size());
    }

    @Test
    public void testCreateTask() {
        // Mock TaskDTO
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setName("Test Task");
        taskDTO.setDueDate(LocalDateTime.now().plusDays(1));
        taskDTO.setPriority(1);
        taskDTO.setEmail("test@example.com");

        // Setup mock behavior
        Mockito.when(userRepository.findByEmail(taskDTO.getEmail())).thenReturn(user);
        Mockito.when(taskRepository.save(Mockito.any(Task.class))).thenReturn(task);
        // Perform the test
        taskService.createTask(taskDTO);
        // No exception thrown indicates success
    }

    @Test
    public void testEditTask() {
        // Mock TaskDTO
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setName("Updated Task Name");
        taskDTO.setDueDate(LocalDateTime.now().plusDays(2));
        taskDTO.setPriority(2);
        taskDTO.setEmail("test@example.com");

        // Mock existing task
        Mockito.when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));

        // Setup mock behavior
        Mockito.when(taskRepository.save(Mockito.any(Task.class))).thenReturn(task);
        // Perform the test
        taskService.editTask(task.getId(), taskDTO);
        // No exception thrown indicates success
    }

    @Test
    public void testDeleteTask() {
        // Mock existing task
        Mockito.when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));
        // Perform the test
        taskService.deleteTask(task.getId());
        // No exception thrown indicates success
    }

    @Test(expected = TaskNotFoundException.class)
    public void testDeleteTaskWithNonExistingTaskId() {
        // Mock non-existing task
        Mockito.when(taskRepository.findById(task.getId())).thenReturn(Optional.empty());
        // Perform the test
        taskService.deleteTask(task.getId());
    }

    @Test
    public void testGetTaskById() {
        // Setup mock behavior
        Mockito.when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));
        // Perform the test
        Task result = taskService.getTaskbyId(task.getId());
        // Assertions
        Assert.assertNotNull(result);
        Assert.assertEquals(task.getId(), result.getId());
    }

    @Test
    public void testGetTaskByIdWithNonExistingTaskId() {
        // Setup mock behavior
        Mockito.when(taskRepository.findById(task.getId())).thenReturn(Optional.empty());
        // Perform the test
        Task result = taskService.getTaskbyId(task.getId());
        // Assertions
        Assert.assertNull(result);
    }
}
