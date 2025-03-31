package com.docuten.demo.controller;

import com.docuten.demo.DTO.KeysDto;
import com.docuten.demo.DTO.UserDto;
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
        } catch (Exception e) { // TODO: handle exceptions correctly (we could get an error with the encryption process)
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> get(@PathVariable UUID userId) {
        try {
            Keys keys = keysService.get(userId);

            return ResponseEntity.ok(keys);
        } catch (Exception e) { // TODO: handle exceptions correctly (we could get an error with the encryption process)
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> delete(@PathVariable UUID userId) {
        try {
            keysService.delete(userId);

            return new ResponseEntity<>("", HttpStatus.OK);
        } catch (Exception e) { // TODO: handle exceptions correctly (we could get an error with the encryption process)
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
