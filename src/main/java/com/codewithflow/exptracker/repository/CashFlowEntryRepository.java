package com.codewithflow.exptracker.repository;

import com.codewithflow.exptracker.entity.CashFlowEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CashFlowEntryRepository extends JpaRepository<CashFlowEntry, Long> {
    Optional<CashFlowEntry> findByIdAndUserId(Long id, Long userId);
}
