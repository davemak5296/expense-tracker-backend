package com.codewithflow.exptracker.repository;

import com.codewithflow.exptracker.entity.EntryType;
import com.codewithflow.exptracker.entity.MainCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MainCategoryRepository extends JpaRepository<MainCategory, Long> {
    Optional<MainCategory> findByNameAndType(String name, EntryType type);
}
