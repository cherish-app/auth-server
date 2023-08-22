package com.saurs.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public record RefreshToken(
        @JsonProperty("refresh_token")
        String refreshToken
) {
}
