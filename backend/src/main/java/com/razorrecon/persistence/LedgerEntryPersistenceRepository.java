package com.razorrecon.persistence;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LedgerEntryPersistenceRepository extends JpaRepository<LedgerEntryEntity, UUID> {
}
