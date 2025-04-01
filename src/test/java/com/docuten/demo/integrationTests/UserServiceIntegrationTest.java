package com.docuten.demo.integrationTests;

import com.docuten.demo.DTO.UserDto;
import com.docuten.demo.exceptions.UserIdNotProvidedException;
import com.docuten.demo.exceptions.UserNotFoundException;
import com.docuten.demo.model.User;
import com.docuten.demo.repository.UserRepository;
import com.docuten.demo.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class UserServiceIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testCreateUser_Success() {
        UserDto userDto = new UserDto();
        userDto.setName("Fernando");
        userDto.setFirstSurname("Alonso");
        userDto.setSecondSurname("Díaz");

        User createdUser = userService.create(userDto);

        assertNotNull(createdUser);
        assertNotNull(createdUser.getId()); // Verify that ID was generated and stored
        assertEquals(userDto.getName(), createdUser.getName());
        assertEquals(userDto.getFirstSurname(), createdUser.getFirstSurname());
        assertEquals(userDto.getSecondSurname(), createdUser.getSecondSurname());

        User dbUser = userRepository.findById(createdUser.getId()).orElseThrow();
        assertEquals(createdUser.getId(), dbUser.getId());
    }

    @Test
    public void testGetUser_Success() throws UserNotFoundException {
        User savedUser = userRepository.save(new User("Fernando", "Alonso", "Díaz"));

        User retrievedUser = userService.get(savedUser.getId());

        assertNotNull(retrievedUser);
        assertEquals(savedUser.getId(), retrievedUser.getId());
    }

    @Test
    public void testUpdateUser_Success() throws UserNotFoundException, UserIdNotProvidedException {
        User savedUser = userRepository.save(new User("Fernando", "Alonso", "Díaz"));

        UserDto updatedUserDto = new UserDto();
        updatedUserDto.setId(savedUser.getId());
        updatedUserDto.setName("Fernando Updated");
        updatedUserDto.setFirstSurname("Alonso");
        updatedUserDto.setSecondSurname("Díaz");

        User updatedUser = userService.update(updatedUserDto);

        assertNotNull(updatedUser);
        assertEquals("Fernando Updated", updatedUser.getName());

        User dbUser = userRepository.findById(savedUser.getId()).orElseThrow();
        assertEquals("Fernando Updated", dbUser.getName()); // Ensure database is updated
    }

    @Test
    public void testDeleteUser_Success() throws UserNotFoundException {
        User savedUser = userRepository.save(new User("Fernando", "Alonso", "Díaz"));

        userService.delete(savedUser.getId());

        assertFalse(userRepository.existsById(savedUser.getId())); // Verify deletion
    }
}
