package com.naveen.scholartracker.service;

import com.naveen.scholartracker.dto.CreateApplicationRequest;
import com.naveen.scholartracker.dto.DisbursementRequest;
import com.naveen.scholartracker.entity.Application;
import com.naveen.scholartracker.entity.Scheme;
import com.naveen.scholartracker.entity.Student;
import com.naveen.scholartracker.enums.ApplicationStatus;
import com.naveen.scholartracker.enums.DisbursementStatus;
import com.naveen.scholartracker.exception.BusinessRuleException;
import com.naveen.scholartracker.exception.ResourceNotFoundException;
import com.naveen.scholartracker.repository.ApplicationRepository;
import com.naveen.scholartracker.repository.SchemeRepository;
import com.naveen.scholartracker.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final StudentRepository studentRepository;
    private final SchemeRepository schemeRepository;

    public ApplicationService(
            ApplicationRepository applicationRepository,
            StudentRepository studentRepository,
            SchemeRepository schemeRepository) {

        this.applicationRepository = applicationRepository;
        this.studentRepository = studentRepository;
        this.schemeRepository = schemeRepository;
    }

    public Application createApplication(CreateApplicationRequest request) {

        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Student not found with id: " + request.getStudentId()
                        ));

        Scheme scheme = schemeRepository.findById(request.getSchemeId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Scheme not found with id: " + request.getSchemeId()
                        ));

        if (!scheme.isActive()) {
            throw new BusinessRuleException(
                    "Cannot apply for an inactive scholarship scheme"
            );
        }

        boolean incomeEligible =
                student.getAnnualIncome().compareTo(scheme.getIncomeLimit()) <= 0;

        boolean marksEligible =
                student.getMarks().compareTo(scheme.getMinimumMarks()) >= 0;

        Application application = new Application();

        application.setStudent(student);
        application.setScheme(scheme);
        application.setDisbursementStatus(DisbursementStatus.PENDING);

        if (incomeEligible && marksEligible) {

            application.setEligibilityPassed(true);
            application.setEligibilityMessage(
                    "Student satisfies income and marks eligibility criteria"
            );
            application.setApplicationStatus(
                    ApplicationStatus.UNDER_REVIEW
            );

        } else {

            application.setEligibilityPassed(false);

            StringBuilder reason = new StringBuilder();

            if (!incomeEligible) {
                reason.append(
                        "Annual income exceeds the scheme limit. "
                );
            }

            if (!marksEligible) {
                reason.append(
                        "Marks are below the required minimum."
                );
            }

            application.setEligibilityMessage(
                    reason.toString().trim()
            );

            application.setApplicationStatus(
                    ApplicationStatus.ELIGIBILITY_FAILED
            );
        }

        return applicationRepository.save(application);
    }

    public List<Application> getAllApplications() {
        return applicationRepository.findAll();
    }

    public Application getApplicationById(Long id) {
        return applicationRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Application not found with id: " + id
                        ));
    }

    public List<Application> getApplicationsByStudent(Long studentId) {

        if (!studentRepository.existsById(studentId)) {
            throw new ResourceNotFoundException(
                    "Student not found with id: " + studentId
            );
        }

        return applicationRepository.findByStudentId(studentId);
    }

    public Application updateDisbursement(
            Long applicationId,
            DisbursementRequest request) {

        Application application = getApplicationById(applicationId);

        if (!application.isEligibilityPassed()) {
            throw new BusinessRuleException(
                    "Ineligible application cannot be disbursed"
            );
        }

        if (application.getApplicationStatus()
                != ApplicationStatus.VERIFIED) {

            throw new BusinessRuleException(
                    "Disbursement cannot be completed until verification is approved"
            );
        }

        if (request.getStatus() != DisbursementStatus.COMPLETED) {
            throw new BusinessRuleException(
                    "Only COMPLETED status can be used for final disbursement"
            );
        }

        if (application.getDisbursementStatus()
                == DisbursementStatus.COMPLETED) {

            throw new BusinessRuleException(
                    "Disbursement has already been completed"
            );
        }

        application.setDisbursementStatus(
                DisbursementStatus.COMPLETED
        );

        application.setApplicationStatus(
                ApplicationStatus.DISBURSED
        );

        return applicationRepository.save(application);
    }
}