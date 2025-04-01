package com.docuten.demo.DTO;

import com.docuten.demo.exceptions.ArgumentRequiredException;

import java.util.UUID;

public class KeysDto {
    private UUID userId;

    public void checkRequiredFields() throws ArgumentRequiredException {
        if (userId == null) {
            throw new ArgumentRequiredException("field userId is required");
        }
    }


    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }
}
