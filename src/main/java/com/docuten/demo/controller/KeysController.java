package com.docuten.demo.controller;

import com.docuten.demo.DTO.KeysDto;
import com.docuten.demo.exceptions.CryptographyException;
import com.docuten.demo.exceptions.KeysNotFoundException;
import com.docuten.demo.exceptions.UserNotFoundException;
import com.docuten.demo.model.Keys;
import com.docuten.demo.service.KeysService;
import jakarta.validation.Valid;
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

    @PostMapping("/")
    public ResponseEntity<?> create(@Valid @RequestBody KeysDto keysDto) {
        try {
            Keys keys = keysService.create(keysDto);

           return ResponseEntity.ok(keys);
        } catch (CryptographyException e) {
            // TODO: log the e.message
            return new ResponseEntity<>("Error: we could not create keys. Please, try again later.", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> delete(@PathVariable UUID userId) {
        try {
            keysService.delete(userId);

            return new ResponseEntity<>("", HttpStatus.OK);
        } catch (KeysNotFoundException | UserNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
