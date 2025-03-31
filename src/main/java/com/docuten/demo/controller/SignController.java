package com.docuten.demo.controller;


import com.docuten.demo.DTO.SignDto;
import com.docuten.demo.service.SignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sign")
public class SignController {

    @Autowired
    SignService signService;

    @PostMapping("/create/")
    public ResponseEntity<String> sign(@RequestBody SignDto signDto) {
        try {
            return ResponseEntity.ok(signService.signDocument(signDto));
        } catch (Exception e) {
            return ResponseEntity.ok("Ok"); // TODO
        }
    }

    @PostMapping("/verify/")
    public ResponseEntity<Boolean> verify(@RequestBody SignDto signDto) {
        try {
            return ResponseEntity.ok(signService.verifySignature(signDto));
        } catch (Exception e) {
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR); // TODO: handle exceptions and return correct errors
        }
    }
}
