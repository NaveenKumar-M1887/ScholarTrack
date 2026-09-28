package com.naveen.scholartracker.service;

import com.naveen.scholartracker.entity.Scheme;
import com.naveen.scholartracker.exception.ResourceNotFoundException;
import com.naveen.scholartracker.repository.SchemeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SchemeService {

    private final SchemeRepository schemeRepository;

    public SchemeService(SchemeRepository schemeRepository) {
        this.schemeRepository = schemeRepository;
    }

    public Scheme createScheme(Scheme scheme) {
        return schemeRepository.save(scheme);
    }

    public List<Scheme> getAllSchemes() {
        return schemeRepository.findAll();
    }

    public List<Scheme> getActiveSchemes() {
        return schemeRepository.findByActiveTrue();
    }

    public Scheme getSchemeById(Long id) {
        return schemeRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Scheme not found with id: " + id
                        ));
    }

    public Scheme updateScheme(Long id, Scheme updatedScheme) {
        Scheme existingScheme = getSchemeById(id);

        existingScheme.setName(updatedScheme.getName());
        existingScheme.setDescription(updatedScheme.getDescription());
        existingScheme.setIncomeLimit(updatedScheme.getIncomeLimit());
        existingScheme.setMinimumMarks(updatedScheme.getMinimumMarks());
        existingScheme.setActive(updatedScheme.isActive());

        return schemeRepository.save(existingScheme);
    }

    public void deleteScheme(Long id) {
        Scheme existingScheme = getSchemeById(id);
        schemeRepository.delete(existingScheme);
    }
}