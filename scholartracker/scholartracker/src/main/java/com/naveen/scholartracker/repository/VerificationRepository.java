package com.naveen.scholartracker.repository;

import com.naveen.scholartracker.entity.Verification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VerificationRepository extends JpaRepository<Verification, Long> {

    Optional<Verification> findByApplicationId(Long applicationId);
}