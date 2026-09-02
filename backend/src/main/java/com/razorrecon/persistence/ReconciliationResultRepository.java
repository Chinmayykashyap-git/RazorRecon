package com.razorrecon.persistence;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ReconciliationResultRepository extends JpaRepository<ReconciliationResultEntity, UUID> {
	List<ReconciliationResultEntity> findByReconciliationId(UUID reconciliationId);
}
