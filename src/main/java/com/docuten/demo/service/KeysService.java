package com.docuten.demo.service;

import com.docuten.demo.DTO.KeysDto;
import com.docuten.demo.model.Keys;
import com.docuten.demo.repository.KeysRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;
import java.util.UUID;

@Service
public class KeysService {
    private final KeysRepository repository;

    @Value("${demo.key}")
    private String secretKeyStr;

    @Autowired
    public KeysService(KeysRepository repository) {
        this.repository = repository;
    }

    public static SecretKey getKeyFromString(String keyString) throws Exception {
        byte[] key = keyString.getBytes(StandardCharsets.UTF_8);
        // Hash the key using SHA-256 to ensure it's 128/192/256 bits
        MessageDigest sha = MessageDigest.getInstance("SHA-256");
        key = sha.digest(key);
        // Use only the first 16 bytes (128 bits) for the AES key
        key = Arrays.copyOf(key, 16);
        return new SecretKeySpec(key, "AES");
    }

    private String encrypt(String data) throws Exception {
        SecretKey secretKey = getKeyFromString(secretKeyStr);
        Cipher cipher = Cipher.getInstance("AES");

        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedBytes = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public String decrypt(String encryptedData) throws Exception {
        SecretKey secretKey = getKeyFromString(secretKeyStr);
        Cipher cipher = Cipher.getInstance("AES");

        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
        return new String(decryptedBytes);
    }



    private Keys createKeys(KeysDto keysDto) throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);

        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        String publicKey = Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());
        String privateKey = Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded());

        String encryptedPrivate = encrypt(privateKey);

        //return new Keys
        return new Keys(keysDto.getUserId(), publicKey, encryptedPrivate);
    }

    public Keys create(KeysDto keysDto) throws Exception {
        Keys keys = repository.save(createKeys(keysDto));

        String decryptedPrivateKey = decrypt(keys.getPrivateKey());

        keys.setPrivateKey(decryptedPrivateKey);

        return keys;
    }

    public Keys get(UUID userId) throws Exception {
        Keys keys = repository.findById(userId).orElseThrow();

        String decryptedPrivateKey = decrypt(keys.getPrivateKey());

        keys.setPrivateKey(decryptedPrivateKey);

        return keys;
    }


    public void delete(UUID userId) throws Exception {
        if (!repository.existsById(userId)) {
            throw new Exception();
        }
        repository.deleteById(userId);
    }

}
