package com.codewithflow.exptracker.repository;

import com.codewithflow.exptracker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
