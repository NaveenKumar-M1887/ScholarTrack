package com.naveen.scholartracker.controller;

import com.naveen.scholartracker.dto.VerificationRequest;
import com.naveen.scholartracker.entity.Verification;
import com.naveen.scholartracker.service.VerificationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/applications")
public class VerificationController {

    private final VerificationService verificationService;

    public VerificationController(VerificationService verificationService) {
        this.verificationService = verificationService;
    }

    @PostMapping("/{applicationId}/verification")
    public ResponseEntity<Verification> verifyApplication(
            @PathVariable Long applicationId,
            @Valid @RequestBody VerificationRequest request) {

        Verification verification = verificationService.verifyApplication(
                applicationId,
                request
        );

        return new ResponseEntity<>(verification, HttpStatus.CREATED);
    }

    @GetMapping("/{applicationId}/verification")
    public ResponseEntity<Verification> getVerification(
            @PathVariable Long applicationId) {

        return ResponseEntity.ok(
                verificationService.getVerificationByApplicationId(applicationId)
        );
    }
}