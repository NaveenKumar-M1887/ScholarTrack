package com.naveen.scholartracker.dto;

import com.naveen.scholartracker.enums.DisbursementStatus;
import jakarta.validation.constraints.NotNull;

public class DisbursementRequest {

    @NotNull(message = "Disbursement status is required")
    private DisbursementStatus status;

    public DisbursementRequest() {
    }

    public DisbursementStatus getStatus() {
        return status;
    }

    public void setStatus(DisbursementStatus status) {
        this.status = status;
    }
}