package com.docuten.demo.DTO;

import com.docuten.demo.exceptions.ArgumentRequiredException;

import java.util.UUID;

public class SignDto {
    private String documentBase64;

    private UUID userId;

    private String documentSignature;

    public void checkRequiredFields(boolean verifying) throws ArgumentRequiredException {
        if (documentBase64 == null) {
           throw new ArgumentRequiredException("the field documentBase64 is required");
        }

        if (userId == null) {
            throw new ArgumentRequiredException("the field userId is required");
        }

        if (verifying && documentSignature == null) {

            throw new ArgumentRequiredException("the field documentSignature is required");
        }
    }

    public String getDocumentBase64() {
        return documentBase64;
    }

    public void setDocumentBase64(String documentBase64) {
        this.documentBase64 = documentBase64;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getDocumentSignature() {
        return documentSignature;
    }

    public void setDocumentSignature(String documentSignature) {
        this.documentSignature = documentSignature;
    }
}
