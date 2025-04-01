package com.docuten.demo.integrationTests;

import com.docuten.demo.DTO.KeysDto;
import com.docuten.demo.DTO.SignDto;
import com.docuten.demo.DTO.UserDto;
import com.docuten.demo.exceptions.CryptographyException;
import com.docuten.demo.exceptions.KeysNotFoundException;
import com.docuten.demo.exceptions.UserNotFoundException;
import com.docuten.demo.model.User;
import com.docuten.demo.repository.KeysRepository;
import com.docuten.demo.service.SignService;
import com.docuten.demo.service.KeysService;
import com.docuten.demo.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class SignServiceIntegrationTest {

    @Autowired
    private SignService signService;

    @Autowired
    private UserService userService;

    @Autowired
    private KeysService keysService;

    @Autowired
    private KeysRepository keysRepository;

    private User testUser;

    @BeforeEach
    public void setUp() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setName("Fernando");
        userDto.setFirstSurname("Alonso");
        userDto.setSecondSurname("Díaz");

        testUser = userService.create(userDto);

        KeysDto keysDto = new KeysDto();
        keysDto.setUserId(testUser.getId());
        keysService.create(keysDto);
    }


    @Test
    public void testSignAndVerifyDocument_Success() throws UserNotFoundException, KeysNotFoundException, CryptographyException {
        SignDto signDto = new SignDto();
        signDto.setUserId(testUser.getId());
        signDto.setDocumentBase64(Base64.getEncoder().encodeToString("Test document content".getBytes()));

        String signature = signService.signDocument(signDto);
        assertNotNull(signature);

        signDto.setDocumentSignature(signature);
        boolean isVerified = signService.verifySignature(signDto);
        assertTrue(isVerified);
    }

    @Test
    public void testSignAndVerifyDocument_InvalidSignature() throws UserNotFoundException, KeysNotFoundException, CryptographyException {
        SignDto signDto = new SignDto();
        signDto.setUserId(testUser.getId());
        signDto.setDocumentBase64(Base64.getEncoder().encodeToString("Test document content".getBytes()));

        String signature = signService.signDocument(signDto);
        assertNotNull(signature);
        signDto.setDocumentBase64(Base64.getEncoder().encodeToString("Test some different content".getBytes()));

        signDto.setDocumentSignature(signature);
        boolean isVerified = signService.verifySignature(signDto);
        assertFalse(isVerified);
    }
}
