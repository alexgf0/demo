package com.docuten.demo.service;

import com.docuten.demo.DTO.SignDto;
import com.docuten.demo.model.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Service
public class SignService {
    @Autowired
    KeysService keysService;

    KeyFactory keyFactory;

    public SignService() throws Exception{
        keyFactory = KeyFactory.getInstance("RSA");
    }

    private PrivateKey getPrivateKey(Keys keys) throws Exception {
        byte[] privateKeyBytes = Base64.getDecoder().decode(keys.getPrivateKey());
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(privateKeyBytes);
        return keyFactory.generatePrivate(spec);
    }

    private PublicKey getPublicKey(Keys keys) throws Exception {
        byte[] publicKeyBytes = Base64.getDecoder().decode(keys.getPublicKey());
        X509EncodedKeySpec spec = new X509EncodedKeySpec(publicKeyBytes);
        return keyFactory.generatePublic(spec);
    }

    private byte[] signDocument(PrivateKey privateKey, byte[] document) throws Exception {
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey);
        signature.update(document);

        return signature.sign();
    }

    public String signDocument(SignDto signDto) throws Exception {
       Keys keys = keysService.get(signDto.getUserId());
       PrivateKey privateKey = getPrivateKey(keys);

       byte[] document = Base64.getDecoder().decode(signDto.getDocumentBase64());

       byte[] signature = signDocument(privateKey, document);

       return Base64.getEncoder().encodeToString(signature);
    }

    public boolean verifySignature(SignDto signDto) throws Exception {
        if (signDto.getDocumentSignature() == null) {
            throw new Exception(); // TODO: use custom Exceptions
        }

        Keys keys = keysService.get(signDto.getUserId());
        PublicKey publicKey = getPublicKey(keys);

        byte[] documentBytes = Base64.getDecoder().decode(signDto.getDocumentBase64());
        byte[] signatureBytes = Base64.getDecoder().decode(signDto.getDocumentSignature());

        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initVerify(publicKey);
        signature.update(documentBytes);

        return signature.verify(signatureBytes);
    }
}
