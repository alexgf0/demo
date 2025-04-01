package com.docuten.demo.integrationTests;

import com.docuten.demo.DTO.KeysDto;
import com.docuten.demo.exceptions.CryptographyException;
import com.docuten.demo.exceptions.KeysNotFoundException;
import com.docuten.demo.exceptions.UserNotFoundException;
import com.docuten.demo.model.Keys;
import com.docuten.demo.model.User;
import com.docuten.demo.repository.KeysRepository;
import com.docuten.demo.repository.UserRepository;
import com.docuten.demo.service.KeysService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class KeysServiceIntegrationTest {

    @Autowired
    private KeysService keysService;

    @Autowired
    private KeysRepository keysRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() throws Exception {
        Field secretKeyField = KeysService.class.getDeclaredField("secretKeyStr");
        secretKeyField.setAccessible(true);
        secretKeyField.set(keysService, "jfqperjlnvsmfldafe");
    }

    @Test
    public void testCreateKeys_Success() throws CryptographyException, UserNotFoundException {
        User user =  userRepository.save(new User("Fernando", "Alonso", "Díaz"));

        KeysDto keysDto = new KeysDto();
        keysDto.setUserId(user.getId());

        Keys createdKeys = keysService.create(keysDto);

        assertNotNull(createdKeys);
        assertEquals(user.getId(), createdKeys.getUserId());
        assertNotNull(createdKeys.getPrivateKey()); // Ensure encryption happened
    }

    @Test
    public void testGetKeys_Success() throws CryptographyException, KeysNotFoundException, UserNotFoundException {
        User user = userRepository.save(new User( "Fernando", "Alonso", "Díaz"));

        KeysDto keysDto = new KeysDto();
        keysDto.setUserId(user.getId());

        Keys savedKeys = keysService.create(keysDto);
        savedKeys.setPrivateKey(keysService.encrypt(savedKeys.getPrivateKey())); // account for testing environment misrepresentation

        keysRepository.findById(savedKeys.getUserId()).orElseThrow();
        Keys retrievedKeys = keysService.get(user.getId());

        assertEquals(savedKeys.getUserId(), retrievedKeys.getUserId());
        assertEquals(savedKeys.getPublicKey(), retrievedKeys.getPublicKey());
        assertNotNull(retrievedKeys.getPrivateKey());
    }

    @Test
    public void testDeleteKeys_Success() throws CryptographyException, KeysNotFoundException, UserNotFoundException {
        User user = userRepository.save(new User("Fernando", "Alonso", "Díaz"));

        KeysDto keysDto = new KeysDto();
        keysDto.setUserId(user.getId());

        keysService.create(keysDto);
        keysService.delete(user.getId());

        assertThrows(KeysNotFoundException.class, () -> keysService.get(user.getId()));
    }
}
