package com.codewithflow.exptracker.service;

import com.codewithflow.exptracker.entity.User;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.function.Function;

public interface JwtService {

    String extractUsername(String token);

    String extractUserId(String token);

    <T> T extractClaim(String token, Function<Claims, T> claimsResolver);

    String generateToken(User user);

    String generateRefreshToken(User user);

    boolean isTokenValid(String token);

    void saveUserToken(User user, String jwtToken);

    void revokeAllUserTokens(User user);
}
