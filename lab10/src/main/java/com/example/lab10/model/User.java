package com.example.lab10.model; // Make sure this matches your package name

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String email;
    // fix in here is hiding the field from the JSON response (Add @JsonIgnore that's all
    @JsonIgnore
    private String password;
}