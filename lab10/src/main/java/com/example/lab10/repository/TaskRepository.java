package com.example.lab10.repository;

import com.example.lab10.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByUserId(Long userId);



    @Query(value = "SELECT * FROM tasks WHERE user_id = ?1", nativeQuery = true)
    List<Task> findAllTasksByUserIdNative(Long userId);
}