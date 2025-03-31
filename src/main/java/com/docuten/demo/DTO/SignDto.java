package com.docuten.demo.DTO;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public class SignDto {
    @NotBlank(message = "Document content is required")
    private String documentBase64;

    private UUID userId;

    private String documentSignature;

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
