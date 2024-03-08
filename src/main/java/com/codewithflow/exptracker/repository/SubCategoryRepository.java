package com.codewithflow.exptracker.repository;

import com.codewithflow.exptracker.entity.EntryType;
import com.codewithflow.exptracker.entity.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubCategoryRepository extends JpaRepository<SubCategory, Long> {
    Optional<SubCategory> findByNameAndType(String name, EntryType type);

    Optional<SubCategory> findByIdAndUserId(Long id, Long userId);
}
