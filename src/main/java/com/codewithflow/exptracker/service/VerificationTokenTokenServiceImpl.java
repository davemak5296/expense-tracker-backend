package com.codewithflow.exptracker.service;

import com.codewithflow.exptracker.entity.User;
import com.codewithflow.exptracker.entity.VerificationToken;
import com.codewithflow.exptracker.repository.VerificationTokenRepository;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Optional;
import java.util.UUID;

@Service
public class VerificationTokenTokenServiceImpl implements VerificationTokenService {

    private final VerificationTokenRepository tokenRepository;

    public VerificationTokenTokenServiceImpl(VerificationTokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Override
    public Optional<VerificationToken> getVerificationToken(String token) {
        return tokenRepository.findByToken(token);
    }

    @Override
    public String createVerificationTokenForUser(User user) {
        String token = UUID.randomUUID().toString();
        VerificationToken vToken = new VerificationToken(token, user);
        tokenRepository.save(vToken);
        return vToken.getToken();
    };

    @Override
    public Boolean isTokenExpired(VerificationToken verificationToken) {
        final Calendar cal = Calendar.getInstance();

        return (verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) < 0;
    }

    @Override
    public void deleteVerificationToken(VerificationToken verificationToken) {
        tokenRepository.delete(verificationToken);
    };
}
