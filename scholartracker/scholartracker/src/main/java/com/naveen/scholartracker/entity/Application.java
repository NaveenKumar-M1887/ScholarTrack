package com.naveen.scholartracker.entity;

import com.naveen.scholartracker.enums.ApplicationStatus;
import com.naveen.scholartracker.enums.DisbursementStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "application")
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne
    @JoinColumn(name = "scheme_id", nullable = false)
    private Scheme scheme;

    @Column(name = "applied_at", nullable = false)
    private LocalDateTime appliedAt;

    @Column(name = "eligibility_passed", nullable = false)
    private boolean eligibilityPassed;

    @Column(name = "eligibility_message", length = 500)
    private String eligibilityMessage;

    @Enumerated(EnumType.STRING)
    @Column(name = "application_status", nullable = false)
    private ApplicationStatus applicationStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "disbursement_status", nullable = false)
    private DisbursementStatus disbursementStatus;

    @PrePersist
    public void setAppliedAt() {
        if (appliedAt == null) {
            appliedAt = LocalDateTime.now();
        }
    }

    public Application() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Scheme getScheme() {
        return scheme;
    }

    public void setScheme(Scheme scheme) {
        this.scheme = scheme;
    }

    public LocalDateTime getAppliedAt() {
        return appliedAt;
    }

    public void setAppliedAt(LocalDateTime appliedAt) {
        this.appliedAt = appliedAt;
    }

    public boolean isEligibilityPassed() {
        return eligibilityPassed;
    }

    public void setEligibilityPassed(boolean eligibilityPassed) {
        this.eligibilityPassed = eligibilityPassed;
    }

    public String getEligibilityMessage() {
        return eligibilityMessage;
    }

    public void setEligibilityMessage(String eligibilityMessage) {
        this.eligibilityMessage = eligibilityMessage;
    }

    public ApplicationStatus getApplicationStatus() {
        return applicationStatus;
    }

    public void setApplicationStatus(ApplicationStatus applicationStatus) {
        this.applicationStatus = applicationStatus;
    }

    public DisbursementStatus getDisbursementStatus() {
        return disbursementStatus;
    }

    public void setDisbursementStatus(DisbursementStatus disbursementStatus) {
        this.disbursementStatus = disbursementStatus;
    }
}