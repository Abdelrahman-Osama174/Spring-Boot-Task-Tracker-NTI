package com.abdelrahman.tasktracker.repo;

import com.abdelrahman.tasktracker.models.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepo extends JpaRepository<Task,Integer> {

    Page<Task> findByCompleted(boolean completed, Pageable pageable);
}
