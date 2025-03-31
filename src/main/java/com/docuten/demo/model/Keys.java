package com.docuten.demo.model;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
public class Keys {

    @Id
    @Column(name = "user_id")
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_keys_user"))
    private UUID userId;

    @Lob
    private String publicKey;

    @Lob
    private String privateKey;

    public Keys() {}

    public Keys(UUID userId, String publicKey, String privateKey) {
        this.userId = userId;
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }
}
