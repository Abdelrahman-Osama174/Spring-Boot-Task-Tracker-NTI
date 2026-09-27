package com.abdelrahman.tasktracker.controller;

import com.abdelrahman.tasktracker.exceptions.MaxTasksException;
import com.abdelrahman.tasktracker.exceptions.TaskNotFoundException;
import com.abdelrahman.tasktracker.models.Task;
import com.abdelrahman.tasktracker.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<?> createTask(@Valid @RequestBody Task task) {
        Task savedTask;
        try {
            savedTask = taskService.create(task);
        } catch (MaxTasksException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(savedTask);
    }


    @GetMapping
    public ResponseEntity<?> getTasks(@RequestParam(required = false) Integer limit,
                                      @RequestParam(required = false) Boolean completed) {
        return ResponseEntity.ok(taskService.findAll(limit, completed));
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getTask(@PathVariable Integer id) {
        Task task;
        try {
            task = taskService.findById(id);
        } catch (TaskNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body(task);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTask(@PathVariable Integer id, @Valid @RequestBody Task task) {
        Task updatedTask;
        try {
            updatedTask = taskService.update(id, task);
        } catch (TaskNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body(updatedTask);
    }

    @PatchMapping("/{id}/complete")
    public ResponseEntity<?> completeTask(@PathVariable Integer id) {
        try {
            taskService.complete(id);
        } catch (TaskNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body("Task with id " + id + " has been completed");

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable Integer id) {
        try {
            taskService.delete(id);
        } catch (TaskNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        return ResponseEntity.ok().body("Task with id " + id + " has been deleted");
    }
}