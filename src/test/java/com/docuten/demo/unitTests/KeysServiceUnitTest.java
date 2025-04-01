package com.docuten.demo.unitTests;

import com.docuten.demo.DTO.KeysDto;
import com.docuten.demo.exceptions.KeysNotFoundException;
import com.docuten.demo.exceptions.UserNotFoundException;
import com.docuten.demo.model.Keys;
import com.docuten.demo.repository.KeysRepository;
import com.docuten.demo.repository.UserRepository;
import com.docuten.demo.service.KeysService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;


@ExtendWith(MockitoExtension.class)
public class KeysServiceUnitTest {

    @Mock
    private KeysRepository keysRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private KeysService keysService;

    private static final String PUBLIC_KEY_BASE64 = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAviYTN1xUxaDNN1lyaSZbI2a43mMgLVVw6Xb1jts2baey2QYoxG/IUjnl9aG4RytJJg/vdSu51PYsmgCpT/SO092RcvBcfNVPFrTkqzIbsOEfvSuaF+/lS+hvByBh0YdmCpKIaX5MEqC22xWPVVJT4QmuUbG6paSuQdNPUwwnii3w/D40xVxa9HxLjxxrICwuNn7qHJEEiATe4pFa5iNCgO19N66jRSJjkqM6WKJ59rixIn69ZS74D8PEBj8TS0p1FXlYmkjmJc8hvUw1innYYhydY9Eear+0I7ZymiblQi7zv7LiGxeWv+LxRM2dwKUPqmPyHscSkwG/DR5BiGVyTwIDAQAB";
    private static final String PRIVATE_KEY = "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQCZMQemoJGaxNSe0A5JUQ/PvX1ZmFg1tqng9oeHqbDNsxmbI6kWj334rzhsmUakRr3yrxP1LoAFvrhgpPNTirjNdpOJJ8TQ8jC+HkFV3yZy7YVJ6CwpBE6Ba6Dnyxb/fvdHLXxgVzfxop+87wrXSQJ0ugZovx6xW0SbY/z327+eVPag/jZQzhaq+XVL2r92dp2atQsbcU7JikFE7SMLUOB73GYPruqfYl4+gevLaUGVd7LoGlB4edCDrwanLHcLk2UzGFOpIyTgliIG+yA0eClBvXvMtOp+I2LZnXhLP7Zu3A1vthWUkL7MJFk0draf22WLc8mUjbM9ybiLlRg8H1iDAgMBAAECggEACyhwk7jvUhQTHowt9jYizjFRn4SCrErlPb8HKSD5gUoRy+SBjOSfuszcccCKTi3e6P3KmNmttn1pRZTOKmAYzNJdtahBZCy30yk1/hC2ZrmZM0Rnbwdixnd17Vw0DSEM1tDf1c6UgxOiyeqpYfvt412nWNrO3AE3UpAqXU1SvC/8OUJpvC5FjrHQU+D9mOoNwSa+rL0j3zPQBf0juyDJMivAsRZYz4VoegQ+VQFb0FkGBvuVie9a5q/OwxzKpHDAYEskXT3EC2z5zTsh9N9j9AK/65WPupB/tjRS0YsmqZd2fmgBbny88H/stI/JOLTXYiOf1CHQRtN14SxwSRzCLQKBgQDBK7AAwEUMv5CuO3MFLHJ+AuXNSZVcFAbi/PX0ljVpiN7hNc5zvn4n3yvQX2d4y6MdTTnXfHPx+FQSJJ3CxqFVL+ovI/sR6TDZyLznpcdAbhVECHloN/Dygje7g2DuohBeiEHhLYlUyQzgT2/Uy1cPCWXtL6+MDaMpq2heErJ7/QKBgQDLBH0bw4lZBlaRuCQTUa1QrgMcRoNYqsAlJk3qTpLaaED3HfvtmCGlHwXb25KeyRab4Rk8+N1OtUFnR/3BzF2SkiDjQmhjIdC+TBU87Yr53AMtUVUZou/X1dOtLoTAoKAGOEYC+7v2IgsLyU0KgiplhnpomCgc6RsSBOutSbwOfwKBgQCIkpmyPj3TgJELDk8I5l7gRq4wkQi/fA1OaKiAT77dqX5i3cbkQFczW26/uOQQHHFroxD1EDFtbzQJS8gaUJL6BpFc2OSGykbWgz1DzgpsvCfe3NrxWzPIUVaUBZP/w6ctHsrtKGPDBQ5MfP1FBqq7n9pncmJ785xbNqvobvGwBQKBgQDJ7I0DMFlL0qIIDb7T+EkKFgnB9eBuMGcsjAyzH7OwyEDViV0p1EailViedKZAiIGUSS9xFZYI0v9aWqBqQXpQbkeHwmVRNDELFWGKWDoCinIegObDuOwVIPiJJ9uiPCDuDsqT7Q3mobWa431dFlpU4Iyfu7cqCjCCm6JNmSkSwQKBgQC2fSN9vaLiO+KoZfLtYt77gO7ALD2T5FmhoTX1Lzcczd6cWsErIg1YA/vUl15JqUVt9MowRYUwixegvwMeHEHyJvMrNMOG2UcdzjemmXijz/19iKK0dLx9S+bkST/qXdHJMlsXnvwNJwvO/MDkgeEg9mZ3o8pFZvTn6iLeNZz/4Q==";
    private static final String ENCRYPTED_PRIVATE_KEY = "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";

    private UUID userId;
    private KeysDto keysDto;
    private Keys testKeys;

    @BeforeEach
    public void setUp() throws Exception {
        userId = UUID.randomUUID();
        keysDto = new KeysDto();
        keysDto.setUserId(userId);

        Field secretKeyField = KeysService.class.getDeclaredField("secretKeyStr");
        secretKeyField.setAccessible(true);
        secretKeyField.set(keysService, "jfqperjlnvsmfldafe");

        testKeys = new Keys(userId, PUBLIC_KEY_BASE64, ENCRYPTED_PRIVATE_KEY);
    }

    @Test
    public void testCreateKeys_Success() throws Exception {
        when(userRepository.existsById(userId)).thenReturn(true);
        when(keysRepository.save(any(Keys.class))).thenReturn(testKeys);

        Keys createdKeys = keysService.create(keysDto);

        assertNotNull(createdKeys);
        assertEquals(createdKeys.getPrivateKey(), PRIVATE_KEY); // assert that it's returning the unencrypted_private_key
        assertEquals(userId, createdKeys.getUserId());

        verify(userRepository, times(1)).existsById(userId);
        verify(keysRepository, times(1)).save(any(Keys.class));
    }

    @Test
    public void testCreateKeys_UserNotFound() {
        when(userRepository.existsById(userId)).thenReturn(false);

        assertThrows(UserNotFoundException.class, () -> keysService.create(keysDto));

        verify(userRepository, times(1)).existsById(userId);
        verify(keysRepository, never()).save(any(Keys.class));
    }

    @Test
    public void testGetKeys_Success() throws KeysNotFoundException, UserNotFoundException {
        when(userRepository.existsById(userId)).thenReturn(true);
        when(keysRepository.findById(userId)).thenReturn(Optional.of(testKeys));

        Keys retrievedKeys = keysService.get(userId);

        assertNotNull(retrievedKeys);
        assertEquals(retrievedKeys.getPrivateKey(), PRIVATE_KEY); // assert that it's returning the unencrypted_private_key
        assertEquals(userId, retrievedKeys.getUserId());

        verify(userRepository, times(1)).existsById(userId);
        verify(keysRepository, times(1)).findById(userId);
    }

    @Test
    public void testGetKeys_UserNotFound() {
        when(userRepository.existsById(userId)).thenReturn(false);

        assertThrows(UserNotFoundException.class, () -> keysService.get(userId));

        verify(userRepository, times(1)).existsById(userId);
        verify(keysRepository, never()).findById(any(UUID.class));
    }

    @Test
    public void testGetKeys_KeysNotFound() {
        when(userRepository.existsById(userId)).thenReturn(true);
        when(keysRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(KeysNotFoundException.class, () -> keysService.get(userId));

        verify(userRepository, times(1)).existsById(userId);
        verify(keysRepository, times(1)).findById(userId);
    }

    @Test
    public void testDeleteKeys_Success() throws KeysNotFoundException, UserNotFoundException {
        when(userRepository.existsById(userId)).thenReturn(true);
        when(keysRepository.existsById(userId)).thenReturn(true);

        keysService.delete(userId);

        verify(userRepository, times(1)).existsById(userId);
        verify(keysRepository, times(1)).existsById(userId);
        verify(keysRepository, times(1)).deleteById(userId);
    }

    @Test
    public void testDeleteKeys_UserNotFound() {
        when(userRepository.existsById(userId)).thenReturn(false);

        assertThrows(UserNotFoundException.class, () -> keysService.delete(userId));

        verify(userRepository, times(1)).existsById(userId);
        verify(keysRepository, never()).deleteById(any(UUID.class));
    }

    @Test
    public void testDeleteKeys_KeysNotFound() {
        when(userRepository.existsById(userId)).thenReturn(true);
        when(keysRepository.existsById(userId)).thenReturn(false);

        assertThrows(KeysNotFoundException.class, () -> keysService.delete(userId));

        verify(userRepository, times(1)).existsById(userId);
        verify(keysRepository, times(1)).existsById(userId);
        verify(keysRepository, never()).deleteById(any(UUID.class));
    }
}
