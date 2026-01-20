package com.example.lab10.service;

import com.example.lab10.dto.TaskDto;
import com.example.lab10.model.Task;
import com.example.lab10.model.User;
import com.example.lab10.repository.TaskRepository;
import com.example.lab10.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskService(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    //Get all tasks for the logged-in user
    public List<Task> getTasksForUser(String username) {
        User user = getUser(username);
        // Using our native query method from Step 2
        return taskRepository.findAllTasksByUserIdNative(user.getId());
    }

    //Create a task for the logged-in user
    public Task createTask(TaskDto taskDto, String username) {
        User user = getUser(username);

        Task task = new Task();
        task.setTitle(taskDto.getTitle());
        task.setDescription(taskDto.getDescription());
        task.setUser(user); // Set ownership!

        return taskRepository.save(task);
    }

    // Delete a task (WITH SECURITY CHECK)
    public void deleteTask(Long taskId, String username) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        User user = getUser(username);

        // SECURITY CHECK: Does this task belong to this user?
        if (!task.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Access Denied: You do not own this task");
        }

        taskRepository.delete(task);
    }

    // Help find the user
    private User getUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}