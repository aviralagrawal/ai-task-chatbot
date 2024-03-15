package com.project.aichatbot.service;

import com.project.aichatbot.dto.TaskDTO;
import com.project.aichatbot.entity.Task;
import com.project.aichatbot.exception.TaskNotFoundException;
import com.project.aichatbot.repository.TaskRepository;
import com.project.aichatbot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;


    @Override
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    @Override
    public void createTask(final TaskDTO taskDTO) {

        Task newTask =  Task.builder()
                .name(taskDTO.getName())
                .dueDate(taskDTO.getDueDate())
                .priority(taskDTO.getPriority())
                .user(userRepository.findByEmail(taskDTO.getEmail()))
                .build();
        taskRepository.save(newTask);
    }

    @Override
    public void editTask(Long taskId, TaskDTO taskDTO) {
        Task existingTask = taskRepository.findById(taskId).orElse(null);

        if (existingTask != null) {
            existingTask.setName(taskDTO.getName());
            existingTask.setDueDate(taskDTO.getDueDate());
            existingTask.setPriority(taskDTO.getPriority());

            taskRepository.save(existingTask);
        }
    }

    @Override
    public void deleteTask(Long taskId) {
        Task existingTask = taskRepository.findById(taskId).orElse(null);

        if (existingTask != null) {
            taskRepository.delete(existingTask);
        } else {
            throw new TaskNotFoundException("Task not found with id: " + taskId);
        }
    }

    @Override
    public Task getTaskbyId(Long taskId) {
        return taskRepository.findById(taskId).orElse(null);
    }
}

