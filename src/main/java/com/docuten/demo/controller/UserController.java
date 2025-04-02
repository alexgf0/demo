package com.docuten.demo.controller;

import com.docuten.demo.DTO.ErrorDto;
import com.docuten.demo.DTO.UserDto;
import com.docuten.demo.exceptions.ArgumentRequiredException;
import com.docuten.demo.exceptions.KeysNotFoundException;
import com.docuten.demo.exceptions.UserNotFoundException;
import com.docuten.demo.model.User;
import com.docuten.demo.service.KeysService;
import com.docuten.demo.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private KeysService keysService;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @PostMapping("/")
    public ResponseEntity<?> create(@RequestBody UserDto userDto) {
        try {
            userDto.checkRequiredFields();
        } catch (ArgumentRequiredException e) {
            logger.debug("Tried to create user without required values: " + e.getMessage());
            return new ResponseEntity<>(new ErrorDto(HttpStatus.BAD_REQUEST, e.getMessage()), HttpStatus.BAD_REQUEST);
        }

        User user = userService.create(userDto);

        return ResponseEntity.ok(user);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable UUID id) {
        try {
            User user = userService.get(id);
            return ResponseEntity.ok(user);
        } catch (UserNotFoundException e) {
            logger.debug("Tried to get not found user with userId: " + id + " - " + e);
            return new ResponseEntity<>(new ErrorDto(HttpStatus.NOT_FOUND, e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }


    @PutMapping("/")
    public ResponseEntity<?> update(@Valid @RequestBody UserDto userDto) {
        try {
            userDto.checkRequiredFields();
            User user = userService.update(userDto);
            return ResponseEntity.ok(user);
        } catch (ArgumentRequiredException e) {
            logger.debug("Tried to update user without required values: " + e);
            return new ResponseEntity<>(new ErrorDto(HttpStatus.BAD_REQUEST, e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (UserNotFoundException e) {
            logger.debug("Tried to update not found user with userId: " + userDto.getId() + " - " + e);
            return new ResponseEntity<>(new ErrorDto(HttpStatus.NOT_FOUND, e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        try {
            keysService.delete(id); // delete associated keys if they exist
        } catch (KeysNotFoundException e) {
            logger.debug("Trying to delete user with id: " + id + " with no keys: " + e);
        }

        try {
            userService.delete(id);
            return new ResponseEntity<>("", HttpStatus.OK);
        } catch (UserNotFoundException e) {
            logger.debug("Trying to delete user with id: " + id + "- :" + e);
            return new ResponseEntity<>(new ErrorDto(HttpStatus.NOT_FOUND, e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

}
