package com.saurs.models;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record Auth(
        @NotNull(message = "Email is required")
        @NotEmpty(message = "Email is required")
        String email,

        @NotNull(message = "Password is required")
        @NotEmpty(message = "Password is required")
        String password
) {
}
