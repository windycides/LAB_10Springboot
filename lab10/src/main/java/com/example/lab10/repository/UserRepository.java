package com.example.lab10.repository;

import com.example.lab10.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional; // <--- Add this import

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Add this method so we can find users by their name
    Optional<User> findByUsername(String username);
}