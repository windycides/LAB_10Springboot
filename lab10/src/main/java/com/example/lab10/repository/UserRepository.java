package com.example.lab10.repository;

import com.example.lab10.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // This empty interface gives us methods like findAll() and save() for free!
}