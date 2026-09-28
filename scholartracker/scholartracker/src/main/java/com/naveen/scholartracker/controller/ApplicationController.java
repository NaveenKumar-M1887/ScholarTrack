package com.naveen.scholartracker.controller;

import com.naveen.scholartracker.dto.CreateApplicationRequest;
import com.naveen.scholartracker.dto.DisbursementRequest;
import com.naveen.scholartracker.entity.Application;
import com.naveen.scholartracker.service.ApplicationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
public class ApplicationController {

    private final ApplicationService applicationService;

    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @PostMapping
    public ResponseEntity<Application> createApplication(
            @Valid @RequestBody CreateApplicationRequest request) {

        Application application = applicationService.createApplication(request);

        return new ResponseEntity<>(application, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Application>> getAllApplications() {
        return ResponseEntity.ok(applicationService.getAllApplications());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Application> getApplicationById(
            @PathVariable Long id) {

        return ResponseEntity.ok(applicationService.getApplicationById(id));
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Application>> getApplicationsByStudent(
            @PathVariable Long studentId) {

        return ResponseEntity.ok(
                applicationService.getApplicationsByStudent(studentId)
        );
    }

    @PutMapping("/{applicationId}/disbursement")
    public ResponseEntity<Application> updateDisbursement(
            @PathVariable Long applicationId,
            @Valid @RequestBody DisbursementRequest request) {

        Application application = applicationService.updateDisbursement(
                applicationId,
                request
        );

        return ResponseEntity.ok(application);
    }
}