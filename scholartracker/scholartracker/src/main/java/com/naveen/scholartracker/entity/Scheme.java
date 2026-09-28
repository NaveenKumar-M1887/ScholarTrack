package com.naveen.scholartracker.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Entity
@Table(name = "scheme")
public class Scheme {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Scheme name is required")
    @Column(nullable = false)
    private String name;

    @Column(length = 1000)
    private String description;

    @NotNull(message = "Income limit is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Income limit cannot be negative")
    @Column(name = "income_limit", nullable = false)
    private BigDecimal incomeLimit;

    @NotNull(message = "Minimum marks are required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Minimum marks cannot be negative")
    @Column(name = "minimum_marks", nullable = false)
    private BigDecimal minimumMarks;

    @Column(nullable = false)
    private boolean active = true;

    public Scheme() {
    }

    public Scheme(String name, String description, BigDecimal incomeLimit,
                  BigDecimal minimumMarks, boolean active) {
        this.name = name;
        this.description = description;
        this.incomeLimit = incomeLimit;
        this.minimumMarks = minimumMarks;
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getIncomeLimit() {
        return incomeLimit;
    }

    public void setIncomeLimit(BigDecimal incomeLimit) {
        this.incomeLimit = incomeLimit;
    }

    public BigDecimal getMinimumMarks() {
        return minimumMarks;
    }

    public void setMinimumMarks(BigDecimal minimumMarks) {
        this.minimumMarks = minimumMarks;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}