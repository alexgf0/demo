package com.docuten.demo.service;

import com.docuten.demo.DTO.KeysDto;
import com.docuten.demo.exceptions.CryptographyException;
import com.docuten.demo.exceptions.KeysNotFoundException;
import com.docuten.demo.exceptions.UserNotFoundException;
import com.docuten.demo.model.Keys;
import com.docuten.demo.repository.KeysRepository;
import com.docuten.demo.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Arrays;
import java.util.Base64;
import java.util.UUID;

@Service
public class KeysService {
    private final KeysRepository repository;

    private final UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(KeysService.class);

    @Value("${demo.key}")
    private String secretKeyStr;

    @Autowired
    public KeysService(KeysRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    private SecretKey getKeyFromString(String keyString) throws CryptographyException {
        byte[] key = keyString.getBytes(StandardCharsets.UTF_8);
        // Hash the key using SHA-256 to ensure it's 128/192/256 bits
        try {
            MessageDigest sha = MessageDigest.getInstance("SHA-256");
            key = sha.digest(key);
            // Use only the first 16 bytes (128 bits) for the AES key
            key = Arrays.copyOf(key, 16);
            return new SecretKeySpec(key, "AES");
        } catch (NoSuchAlgorithmException e) {
            logger.error(e.getMessage());
            throw new CryptographyException(e.getMessage());
        }
    }

    public String encrypt(String data) throws CryptographyException {
        try {
            SecretKey secretKey = getKeyFromString(secretKeyStr);
            Cipher cipher = Cipher.getInstance("AES");

            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encryptedBytes = cipher.doFinal(data.getBytes());
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (NoSuchPaddingException | IllegalBlockSizeException | NoSuchAlgorithmException | BadPaddingException |
                 InvalidKeyException e) {
            logger.error(e.getMessage());
            throw new CryptographyException(e.getMessage());
        }
    }

    private String decrypt(String encryptedData) throws CryptographyException {
        try {
            SecretKey secretKey = getKeyFromString(secretKeyStr);
            Cipher cipher = Cipher.getInstance("AES");

            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
            return new String(decryptedBytes);
        } catch (NoSuchPaddingException | IllegalBlockSizeException | NoSuchAlgorithmException | BadPaddingException |
                 InvalidKeyException e) {
            logger.error(e.getMessage());
            throw new CryptographyException(e.getMessage());
        }
    }


    private Keys createKeys(KeysDto keysDto) throws CryptographyException {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");

            keyPairGenerator.initialize(2048);

            KeyPair keyPair = keyPairGenerator.generateKeyPair();

            String publicKey = Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());
            String privateKey = Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded());

            String encryptedPrivate = encrypt(privateKey);

            return new Keys(keysDto.getUserId(), publicKey, encryptedPrivate);
        } catch (NoSuchAlgorithmException e) {
            logger.error(e.getMessage());
            throw new CryptographyException(e.getMessage());
        }
    }

    public Keys create(KeysDto keysDto) throws CryptographyException, UserNotFoundException {
        if (!userRepository.existsById(keysDto.getUserId())) {
            throw new UserNotFoundException();
        }

        Keys keys = repository.save(createKeys(keysDto));

        String decryptedPrivateKey = decrypt(keys.getPrivateKey());

        keys.setPrivateKey(decryptedPrivateKey);

        return keys;
    }

    public Keys get(UUID userId) throws KeysNotFoundException, UserNotFoundException{
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException();
        }
        Keys keys = repository.findById(userId).orElseThrow(KeysNotFoundException::new);

        String decryptedPrivateKey = decrypt(keys.getPrivateKey());
        keys.setPrivateKey(decryptedPrivateKey);

        return keys;
    }

    public void delete(UUID userId) throws KeysNotFoundException, UserNotFoundException {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException();
        }

        if (!repository.existsById(userId)) {
            throw new KeysNotFoundException();
        }
        repository.deleteById(userId);
    }

}
