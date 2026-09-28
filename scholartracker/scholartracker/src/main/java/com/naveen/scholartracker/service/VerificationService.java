package com.naveen.scholartracker.service;

import com.naveen.scholartracker.dto.VerificationRequest;
import com.naveen.scholartracker.entity.Application;
import com.naveen.scholartracker.entity.Verification;
import com.naveen.scholartracker.enums.ApplicationStatus;
import com.naveen.scholartracker.enums.VerificationDecision;
import com.naveen.scholartracker.exception.BusinessRuleException;
import com.naveen.scholartracker.exception.ResourceNotFoundException;
import com.naveen.scholartracker.repository.ApplicationRepository;
import com.naveen.scholartracker.repository.VerificationRepository;
import org.springframework.stereotype.Service;

@Service
public class VerificationService {

    private final VerificationRepository verificationRepository;
    private final ApplicationRepository applicationRepository;

    public VerificationService(
            VerificationRepository verificationRepository,
            ApplicationRepository applicationRepository) {

        this.verificationRepository = verificationRepository;
        this.applicationRepository = applicationRepository;
    }

    public Verification verifyApplication(
            Long applicationId,
            VerificationRequest request) {

        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Application not found with id: " + applicationId
                        ));

        if (!application.isEligibilityPassed()) {
            throw new BusinessRuleException(
                    "Ineligible application cannot be sent for manual verification"
            );
        }

        if (application.getApplicationStatus()
                != ApplicationStatus.UNDER_REVIEW) {

            throw new BusinessRuleException(
                    "Application is not available for verification"
            );
        }

        if (verificationRepository
                .findByApplicationId(applicationId)
                .isPresent()) {

            throw new BusinessRuleException(
                    "Application has already been verified"
            );
        }

        Verification verification = new Verification();

        verification.setApplication(application);
        verification.setVerifierName(request.getVerifierName());
        verification.setDecision(request.getDecision());
        verification.setRemarks(request.getRemarks());

        if (request.getDecision()
                == VerificationDecision.APPROVED) {

            application.setApplicationStatus(
                    ApplicationStatus.VERIFIED
            );

        } else {

            application.setApplicationStatus(
                    ApplicationStatus.REJECTED
            );
        }

        applicationRepository.save(application);

        return verificationRepository.save(verification);
    }

    public Verification getVerificationByApplicationId(Long applicationId) {

        return verificationRepository
                .findByApplicationId(applicationId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Verification not found for application id: "
                                        + applicationId
                        ));
    }
}