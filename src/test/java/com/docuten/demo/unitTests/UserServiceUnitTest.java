package com.docuten.demo.unitTests;

import com.docuten.demo.DTO.UserDto;
import com.docuten.demo.exceptions.UserNotFoundException;
import com.docuten.demo.model.User;
import com.docuten.demo.repository.UserRepository;
import com.docuten.demo.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceUnitTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private UserDto userDto;
    private User testUser;
    private UUID userId;

    @BeforeEach
    public void setUp() {
        userId = UUID.randomUUID();
        userDto = new UserDto();
        userDto.setId(userId);
        userDto.setName("Fernando");
        userDto.setFirstSurname("Alonso");
        userDto.setSecondSurname("Díaz");

        testUser = new User(userId, "Fernando", "Alonso", "Díaz");
    }

    @Test
    public void testCreateUser_Success() {
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User createdUser = userService.create(userDto);

        assertNotNull(createdUser);
        assertEquals(userDto.getName(), createdUser.getName());

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void testGetUser_Success() throws UserNotFoundException {
        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));

        User retrievedUser = userService.get(userId);

        assertNotNull(retrievedUser);
        assertEquals(testUser.getId(), retrievedUser.getId());

        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    public void testGetUser_NotFound() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.get(userId));

        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    public void testUpdateUser_Success() throws UserNotFoundException {
        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User updatedUser = userService.update(userDto);

        assertNotNull(updatedUser);
        assertEquals(userDto.getName(), updatedUser.getName());

        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void testUpdateUser_UserNotFound() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.update(userDto));

        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void testDeleteUser_Success() throws UserNotFoundException {
        when(userRepository.existsById(userId)).thenReturn(true);

        userService.delete(userId);

        verify(userRepository, times(1)).existsById(userId);
        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    public void testDeleteUser_NotFound() {
        when(userRepository.existsById(userId)).thenReturn(false);

        assertThrows(UserNotFoundException.class, () -> userService.delete(userId));

        verify(userRepository, times(1)).existsById(userId);
        verify(userRepository, never()).deleteById(any(UUID.class));
    }
}
