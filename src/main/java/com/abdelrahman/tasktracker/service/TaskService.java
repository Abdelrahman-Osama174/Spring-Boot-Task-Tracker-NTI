package com.abdelrahman.tasktracker.service;

import com.abdelrahman.tasktracker.exceptions.MaxTasksException;
import com.abdelrahman.tasktracker.exceptions.TaskNotFoundException;
import com.abdelrahman.tasktracker.models.Task;
import com.abdelrahman.tasktracker.repo.TaskRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class TaskService {

    private final TaskRepo taskRepo;

    @Value("${tasktracker.max-tasks}")
    private int maxTasks;

    @Value("${tasktracker.default-page-size}")
    private int defaultPageSize;

    public TaskService(TaskRepo taskRepo) {
        this.taskRepo = taskRepo;
    }


    public Task create(Task task) {
        if (taskRepo.count() >= maxTasks)
            throw new MaxTasksException("Maximum number of tasks has been reached");

        task.setCompleted(false);

        log.info("Creating task {}", task);
        return taskRepo.save(task);
    }


    public List<Task> findAll(Integer limit, Boolean completed) {
        int pageSize = limit != null ? limit : defaultPageSize;
        Pageable pageable = PageRequest.of(0, (pageSize));

        if (completed != null) {
            return taskRepo.findByCompleted(completed, pageable).getContent();
        }

        return taskRepo.findAll(pageable).getContent();
    }


    public Task findById(Integer id) {
        Task task = taskRepo.findById(id).orElse(null);
        if (task == null) {
            log.error("Task with id {} not found", id);
            throw new TaskNotFoundException("Task with id " + id + " not found");
        }

        return task;
    }


    public Task update(Integer id, Task updatedTask) {
        Task task = findById(id);
        if (task == null)
            throw new TaskNotFoundException("Task with id " + id + " not found");

        task.setTitle(updatedTask.getTitle());
        task.setDescription(updatedTask.getDescription());
        task.setCompleted(updatedTask.getCompleted() != null && updatedTask.getCompleted());
        task.setDueDate(updatedTask.getDueDate());

        log.info("Updating task #{}, {}", task.getId(), task.getTitle());
        return taskRepo.save(task);
    }


    public void complete(Integer id) {
        Task task = findById(id);
        if (task == null)
            throw new TaskNotFoundException("Task with id " + id + " not found");

        task.setCompleted(true);

        log.info("Completing task #{}, {}", task.getId(), task.getTitle());
        taskRepo.save(task);
    }


    public void delete(Integer id) {
        Task task = findById(id);
        if (task == null)
            throw new TaskNotFoundException("Task with id " + id + " not found");

        log.info("Deleting task #{}, {}", task.getId(), task.getTitle());
        taskRepo.delete(task);
    }
}