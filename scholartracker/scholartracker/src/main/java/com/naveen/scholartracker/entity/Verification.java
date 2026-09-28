package com.naveen.scholartracker.entity;

import com.naveen.scholartracker.enums.VerificationDecision;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "verification")
public class Verification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "application_id", nullable = false, unique = true)
    private Application application;

    @Column(name = "verifier_name", nullable = false)
    private String verifierName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VerificationDecision decision;

    @Column(length = 1000)
    private String remarks;

    @Column(name = "verified_at", nullable = false)
    private LocalDateTime verifiedAt;

    @PrePersist
    public void setVerifiedAt() {
        if (verifiedAt == null) {
            verifiedAt = LocalDateTime.now();
        }
    }

    public Verification() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public String getVerifierName() {
        return verifierName;
    }

    public void setVerifierName(String verifierName) {
        this.verifierName = verifierName;
    }

    public VerificationDecision getDecision() {
        return decision;
    }

    public void setDecision(VerificationDecision decision) {
        this.decision = decision;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public LocalDateTime getVerifiedAt() {
        return verifiedAt;
    }

    public void setVerifiedAt(LocalDateTime verifiedAt) {
        this.verifiedAt = verifiedAt;
    }
}