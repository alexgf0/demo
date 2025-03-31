package com.docuten.demo.DTO;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class KeysDto {
    @NotNull(message = "User id is required")
    private UUID userId;

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }
}
