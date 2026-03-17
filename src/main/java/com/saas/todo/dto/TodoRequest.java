package com.saas.todo.dto;

import jakarta.validation.constraints.NotBlank;

public record TodoRequest(
        @NotBlank String title,
        String description
) {}
