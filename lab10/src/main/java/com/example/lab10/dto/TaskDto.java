package com.example.lab10.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TaskDto {

    @NotBlank(message = "Title is required")
    private String title;

    private String description;
}