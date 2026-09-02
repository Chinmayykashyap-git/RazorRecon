package com.razorrecon.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.razorrecon.model.ReconciliationRecord;

public interface ReconciliationRepository extends JpaRepository<ReconciliationRecord, Long> {
}
