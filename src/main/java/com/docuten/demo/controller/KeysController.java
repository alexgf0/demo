package com.docuten.demo.controller;

import com.docuten.demo.DTO.ErrorDto;
import com.docuten.demo.DTO.KeysDto;
import com.docuten.demo.exceptions.ArgumentRequiredException;
import com.docuten.demo.exceptions.CryptographyException;
import com.docuten.demo.exceptions.KeysNotFoundException;
import com.docuten.demo.exceptions.UserNotFoundException;
import com.docuten.demo.model.Keys;
import com.docuten.demo.service.KeysService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/keys")
public class KeysController {
    @Autowired
    private KeysService keysService;

    private static final Logger logger = LoggerFactory.getLogger(KeysController.class);

    @PostMapping("/")
    public ResponseEntity<?> create(@RequestBody KeysDto keysDto) {
        try {
            keysDto.checkRequiredFields();
            Keys keys = keysService.create(keysDto);

           return ResponseEntity.ok(keys);
        } catch (ArgumentRequiredException e) {
            logger.error("Tried to create keys without required fields : " + e);
            return new ResponseEntity<>(new ErrorDto(HttpStatus.BAD_REQUEST, e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (CryptographyException e) {
            logger.error("Tried to create keys with CryptographyException: " + e);
            return new ResponseEntity<>(new ErrorDto(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error: we could not create keys. Please, try again later."), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (UserNotFoundException e) {
            logger.debug("Tried to create keys for non-existing user " + keysDto.getUserId() + ": " + e);
            return new ResponseEntity<>(new ErrorDto(HttpStatus.NOT_FOUND, e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> delete(@PathVariable UUID userId) {
        try {
            keysService.delete(userId);

            return new ResponseEntity<>("", HttpStatus.OK);
        } catch (KeysNotFoundException | UserNotFoundException e) {
            logger.debug("Tried to delete keys with Exception: " + e);
            return new ResponseEntity<>(new ErrorDto(HttpStatus.NOT_FOUND, e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }
}
