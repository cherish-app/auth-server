package com.saurs.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record Register(
        @NotNull(message = "First name is required")
        @NotEmpty(message = "First name is required")
        @JsonProperty("first_name")
        String firstName,

        @NotNull(message = "Last name is required")
        @NotEmpty(message = "Last name is required")
        @JsonProperty("last_name")
        String lastName,

        @NotNull(message = "Email is required")
        @NotEmpty(message = "Email is required")
        String email,

        @NotNull(message = "Password is required")
        @NotEmpty(message = "Password is required")
        String password
) {
}
