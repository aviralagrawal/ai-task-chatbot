package com.project.aichatbot.service;

import com.project.aichatbot.dto.TaskDTO;
import com.project.aichatbot.entity.Task;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface TaskService {

    List<Task> getAllTasks();

    void createTask(TaskDTO taskDTO);

    void editTask(Long taskId, TaskDTO taskDTO);

    void deleteTask(Long taskId);

    Task getTaskbyId(Long taskId);
}
