package com.naveen.scholartracker.dto;

import com.naveen.scholartracker.enums.VerificationDecision;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class VerificationRequest {

    @NotBlank(message = "Verifier name is required")
    private String verifierName;

    @NotNull(message = "Verification decision is required")
    private VerificationDecision decision;

    private String remarks;

    public VerificationRequest() {
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
}