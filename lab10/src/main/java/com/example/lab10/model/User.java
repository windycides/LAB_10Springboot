package com.example.lab10.model; // Make sure this matches your package name

import jakarta.persistence.*;
import lombok.Data;

@Data // Generates Getters, Setters, and toString automatically
@Entity // Tells Hibernate this maps to a database table
@Table(name = "users") // Specifies the exact table name
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String email;
    private String password;
}