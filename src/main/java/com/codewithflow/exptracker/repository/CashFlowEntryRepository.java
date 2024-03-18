package com.codewithflow.exptracker.repository;

import com.codewithflow.exptracker.entity.CashFlowEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface CashFlowEntryRepository extends JpaRepository<CashFlowEntry, Long>, JpaSpecificationExecutor<CashFlowEntry> {
    Optional<CashFlowEntry> findByIdAndUserId(Long id, Long userId);
}
