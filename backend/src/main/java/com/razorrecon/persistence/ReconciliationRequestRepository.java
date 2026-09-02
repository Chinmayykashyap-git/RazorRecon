package com.razorrecon.persistence;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ReconciliationRequestRepository extends JpaRepository<ReconciliationRequestEntity, UUID> {
    Optional<ReconciliationRequestEntity> findByRequestHash(String requestHash);
}
