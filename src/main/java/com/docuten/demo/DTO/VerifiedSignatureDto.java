package com.docuten.demo.DTO;

import java.util.UUID;

public class VerifiedSignatureDto {
    private boolean isValid;

    private UUID userId;

    private String documentBase64;

    private String signature;

    public VerifiedSignatureDto(boolean isValid, UUID userId, String documentBase64, String signature) {
        this.isValid = isValid;
        this.userId = userId;
        this.documentBase64 = documentBase64;
        this.signature = signature;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getDocumentBase64() {
        return documentBase64;
    }

    public void setDocumentBase64(String documentBase64) {
        this.documentBase64 = documentBase64;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}
