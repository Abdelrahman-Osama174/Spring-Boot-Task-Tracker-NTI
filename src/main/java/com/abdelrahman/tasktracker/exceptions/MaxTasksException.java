package com.abdelrahman.tasktracker.exceptions;

public class MaxTasksException extends RuntimeException {
    public MaxTasksException(String message) {
        super(message);
    }
}
