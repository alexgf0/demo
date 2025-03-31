package com.docuten.demo.controller;


import com.docuten.demo.DTO.SignDto;
import com.docuten.demo.exceptions.CryptographyException;
import com.docuten.demo.exceptions.KeysNotFoundException;
import com.docuten.demo.exceptions.SignatureNotProvidedException;
import com.docuten.demo.exceptions.UserNotFoundException;
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
        } catch (UserNotFoundException | KeysNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (CryptographyException e) {
            // TODO: log the e.message
            return new ResponseEntity<>("Error: we could not create keys. Please, try again later.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/verify/")
    public ResponseEntity<Boolean> verify(@RequestBody SignDto signDto) {
        try {
            return ResponseEntity.ok(signService.verifySignature(signDto));
        } catch (CryptographyException e) {
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (SignatureNotProvidedException e) {
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        } catch (UserNotFoundException | KeysNotFoundException e) {
            return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
        }
    }
}
