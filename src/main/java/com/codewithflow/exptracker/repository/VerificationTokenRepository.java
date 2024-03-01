package com.codewithflow.exptracker.repository;

import com.codewithflow.exptracker.entity.User;
import com.codewithflow.exptracker.entity.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {

    Optional<VerificationToken> findByToken(String token);

    Optional<VerificationToken> findByUser(User user);

}