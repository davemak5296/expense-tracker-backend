package com.codewithflow.exptracker.repository;

import com.codewithflow.exptracker.entity.CashFlowEntry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CashFlowEntryRepository extends JpaRepository<CashFlowEntry, Long> {
}
