package com.docuten.demo.service;

import com.docuten.demo.DTO.SignDto;
import com.docuten.demo.exceptions.*;
import com.docuten.demo.model.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Service
public class SignService {
    @Autowired
    KeysService keysService;

    KeyFactory keyFactory;

    private static final Logger logger = LoggerFactory.getLogger(SignService.class);

    public SignService() throws NoSuchAlgorithmException {
        keyFactory = KeyFactory.getInstance("RSA");
    }

    private PrivateKey getPrivateKey(Keys keys) throws CryptographyException {
        byte[] privateKeyBytes = Base64.getDecoder().decode(keys.getPrivateKey());
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(privateKeyBytes);
        try {
            return keyFactory.generatePrivate(spec);
        } catch (InvalidKeySpecException e) {
            logger.error(e.getMessage());
            throw new CryptographyException(e.getMessage());
        }
    }

    private PublicKey getPublicKey(Keys keys) throws CryptographyException {
        byte[] publicKeyBytes = Base64.getDecoder().decode(keys.getPublicKey());
        X509EncodedKeySpec spec = new X509EncodedKeySpec(publicKeyBytes);
        try {
            return keyFactory.generatePublic(spec);
        } catch (InvalidKeySpecException e) {
            logger.error(e.getMessage());
            throw new CryptographyException(e.getMessage());
        }
    }

    private byte[] signDocument(PrivateKey privateKey, byte[] document) throws CryptographyException {
        try {
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initSign(privateKey);
            signature.update(document);

            return signature.sign();
        } catch (InvalidKeyException | NoSuchAlgorithmException | SignatureException e) {
            logger.error(e.getMessage());
            throw new CryptographyException(e.getMessage());
        }
    }

    public String signDocument(SignDto signDto)
            throws UserNotFoundException, KeysNotFoundException, CryptographyException, InvalidDocumentException
    {
        Keys keys = keysService.get(signDto.getUserId());
        PrivateKey privateKey = getPrivateKey(keys);

        byte[] documentBytes;

        try {
            documentBytes = Base64.getDecoder().decode(signDto.getDocumentBase64());
        } catch (IllegalArgumentException e) {
            throw new InvalidDocumentException("Invalid documentBase64: " + e.getMessage());
        }

        byte[] signature = signDocument(privateKey, documentBytes);

        return Base64.getEncoder().encodeToString(signature);
    }

    public boolean verifySignature(SignDto signDto) throws CryptographyException, UserNotFoundException,
            KeysNotFoundException, InvalidSignatureException, InvalidDocumentException
    {
        Keys keys = keysService.get(signDto.getUserId());
        PublicKey publicKey = getPublicKey(keys);

        byte[] documentBytes;
        byte[] signatureBytes;

        try {
            documentBytes = Base64.getDecoder().decode(signDto.getDocumentBase64());
        } catch (IllegalArgumentException e) {
            throw new InvalidDocumentException("Invalid documentBase64: " + e.getMessage());
        }

        try {
           signatureBytes = Base64.getDecoder().decode(signDto.getDocumentSignature());
        } catch (IllegalArgumentException e) {
            throw new InvalidSignatureException("Invalid Signature:documentSignature: " + e.getMessage());
        }


        try {
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initVerify(publicKey);
            signature.update(documentBytes);

            return signature.verify(signatureBytes);
        } catch (InvalidKeyException | NoSuchAlgorithmException | SignatureException e) {
            logger.error(e.getMessage());
            throw new CryptographyException(e.getMessage());
        } catch (IllegalArgumentException e) {
            throw new InvalidSignatureException(e.getMessage());
        }
    }
}
