package com.razorrecon.persistence;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SettlementPersistenceRepository extends JpaRepository<SettlementEntity, UUID> {
}
