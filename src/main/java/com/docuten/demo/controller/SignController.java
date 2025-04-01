package com.docuten.demo.controller;


import com.docuten.demo.DTO.CompletedSignatureDto;
import com.docuten.demo.DTO.ErrorDto;
import com.docuten.demo.DTO.SignDto;
import com.docuten.demo.DTO.VerifiedSignatureDto;
import com.docuten.demo.exceptions.*;
import com.docuten.demo.service.SignService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(SignController.class);

    @PostMapping("/create/")
    public ResponseEntity<?> sign(@RequestBody SignDto signDto) {
        try {
            signDto.checkRequiredFields(false);
            String signature = signService.signDocument(signDto);
            return ResponseEntity.ok(new CompletedSignatureDto(signDto.getUserId(), signDto.getDocumentBase64(), signature));
        } catch (ArgumentRequiredException e) {
            logger.debug("Tried to sign document with missing fields: " + e);
            return new ResponseEntity<>(new ErrorDto(HttpStatus.BAD_REQUEST, e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (UserNotFoundException | KeysNotFoundException e) {
            logger.debug("Tried to sign document with Exception: " + e);
            return new ResponseEntity<>(new ErrorDto(HttpStatus.NOT_FOUND, e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (InvalidDocumentException e) {
            logger.debug("Tried to sign document with invalid document: " + e);
            return new ResponseEntity<>(new ErrorDto(HttpStatus.BAD_REQUEST, e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (CryptographyException e) {
            logger.error("Tried to sign document with CryptographyException: " + e);
            return new ResponseEntity<>(new ErrorDto(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/verify/")
    public ResponseEntity<?> verify(@RequestBody SignDto signDto) {
        try {
            signDto.checkRequiredFields(true);

            boolean isValid = signService.verifySignature(signDto);
            return ResponseEntity.ok(new VerifiedSignatureDto(isValid, signDto.getUserId(), signDto.getDocumentBase64(),
                    signDto.getDocumentSignature()));
        } catch (ArgumentRequiredException e) {
            logger.debug("Tried to verify document with missing fields: " + e);
            return new ResponseEntity<>(new ErrorDto(HttpStatus.BAD_REQUEST, e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (CryptographyException e) {
            logger.error("Tried to verify document with CryptographyException: " + e);
            return new ResponseEntity<>(new ErrorDto(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (InvalidSignatureException | InvalidDocumentException e) {
            logger.debug("Tried to verify document with invalid input: " + e);
            return new ResponseEntity<>(new ErrorDto(HttpStatus.BAD_REQUEST, e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (UserNotFoundException | KeysNotFoundException e) {
            logger.debug("Tried to verify document with Exception: " + e);
            return new ResponseEntity<>(new ErrorDto(HttpStatus.NOT_FOUND, e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }
}
