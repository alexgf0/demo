package com.docuten.demo.controller;

import com.docuten.demo.DTO.UserDto;
import com.docuten.demo.exceptions.KeysNotFoundException;
import com.docuten.demo.exceptions.UserIdNotProvidedException;
import com.docuten.demo.exceptions.UserNotFoundException;
import com.docuten.demo.model.User;
import com.docuten.demo.service.KeysService;
import com.docuten.demo.service.UserService;
import jakarta.validation.Valid;
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

    @PostMapping("/")
    public ResponseEntity<?> create(@Valid @RequestBody UserDto userDto) {
        User user = userService.create(userDto);

        return ResponseEntity.ok(user);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable UUID id) {
        try {
            User user = userService.get(id);
            return ResponseEntity.ok(user);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }


    @PutMapping("/")
    public ResponseEntity<?> update(@Valid @RequestBody UserDto userDto) {
        try {
            User user = userService.update(userDto);
            return ResponseEntity.ok(user);
        } catch (UserIdNotProvidedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        try {
            keysService.delete(id); // delete associated keys if they exist
        } catch (KeysNotFoundException e) {
           // TODO: log it
        }

        try {
            userService.delete(id);
            return new ResponseEntity<>("", HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

}
