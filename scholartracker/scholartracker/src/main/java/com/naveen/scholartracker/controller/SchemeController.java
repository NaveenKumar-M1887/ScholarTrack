package com.naveen.scholartracker.controller;

import com.naveen.scholartracker.entity.Scheme;
import com.naveen.scholartracker.service.SchemeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/schemes")
public class SchemeController {

    private final SchemeService schemeService;

    public SchemeController(SchemeService schemeService) {
        this.schemeService = schemeService;
    }

    @PostMapping
    public ResponseEntity<Scheme> createScheme(@Valid @RequestBody Scheme scheme) {
        Scheme createdScheme = schemeService.createScheme(scheme);
        return new ResponseEntity<>(createdScheme, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Scheme>> getAllSchemes() {
        return ResponseEntity.ok(schemeService.getAllSchemes());
    }

    @GetMapping("/active")
    public ResponseEntity<List<Scheme>> getActiveSchemes() {
        return ResponseEntity.ok(schemeService.getActiveSchemes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Scheme> getSchemeById(@PathVariable Long id) {
        return ResponseEntity.ok(schemeService.getSchemeById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Scheme> updateScheme(
            @PathVariable Long id,
            @Valid @RequestBody Scheme scheme) {

        return ResponseEntity.ok(schemeService.updateScheme(id, scheme));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteScheme(@PathVariable Long id) {
        schemeService.deleteScheme(id);
        return ResponseEntity.noContent().build();
    }
}