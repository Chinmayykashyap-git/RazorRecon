package com.razorrecon.persistence;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ExceptionPersistenceRepository extends JpaRepository<ExceptionEntity, UUID> {
    List<ExceptionEntity> findAllByOrderByCreatedAtDesc();
}
