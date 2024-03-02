package com.codewithflow.exptracker.service;

import com.codewithflow.exptracker.entity.User;
import com.codewithflow.exptracker.entity.VerificationToken;

import java.util.Optional;

public interface VerificationTokenService {

    Optional<VerificationToken> getVerificationToken(String token);

    String createVerificationTokenForUser(User user);

    Boolean isTokenExpired(VerificationToken verificationToken);

    void deleteVerificationToken(VerificationToken verificationToken);

}
