package com.codewithflow.exptracker.service;

import com.codewithflow.exptracker.entity.IssuedJwt;
import com.codewithflow.exptracker.entity.User;
import com.codewithflow.exptracker.enumtype.TokenType;
import com.codewithflow.exptracker.repository.IssuedJwtRepository;
import com.codewithflow.exptracker.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.*;
import java.util.function.Function;

@Service
public class JwtServiceImpl implements JwtService {

    @Value("${exptracker.security.jwt.secret-key}")
    private String secretKey;
    @Value("${exptracker.security.jwt.expiration}")
    private long jwtExpiration;
    @Value("${exptracker.security.jwt.refresh-token.expiration}")
    private long refreshExpiration;

    private final IssuedJwtRepository issuedJwtRepository;
    private final UserRepository userRepository;

    public JwtServiceImpl(IssuedJwtRepository issuedJwtRepository, UserRepository userRepository) {
        this.issuedJwtRepository = issuedJwtRepository;
        this.userRepository = userRepository;
    }

    @Override
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String extractUserId(String token) {
        return extractClaim(token, claims -> claims.get("user_id", String.class));
    }

    @Override
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    @Override
    public String generateToken(User user) {
        return generateToken(new HashMap<>(), user);
    }

    public String generateToken( Map<String, Object> extraClaims, User user ) {
        return buildToken(extraClaims, user, jwtExpiration);
    }

    @Override
    public String generateRefreshToken( User user ) {
        return buildToken(new HashMap<>(), user, refreshExpiration);
    }

    private String buildToken( Map<String, Object> extraClaims, User user, long expiration ) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(user.getEmail())
                .claim("user_id", user.getId().toString())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public boolean isTokenValid(String token) {
        final String userEmail = extractUsername(token);
        return (userRepository.findByEmail(userEmail).isPresent()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    public void saveUserToken(User user, String token) {
        IssuedJwt jwt = new IssuedJwt();
        jwt.setToken(token);
        jwt.setTokenType(TokenType.BEARER);
        jwt.setExpired(false);
        jwt.setRevoked(false);
        jwt.setUser(user);

        issuedJwtRepository.save(jwt);
    }

    @Override
    public void revokeAllUserTokens(User user) {
        Optional<List<IssuedJwt>> issuedJwts = issuedJwtRepository.findAllValidTokenByUser(user.getId());
        if (issuedJwts.isEmpty()) {
            return;
        }

        issuedJwts.get().forEach(issuedJwt -> {
            issuedJwt.setRevoked(true);
            issuedJwt.setExpired(true);
        });

        issuedJwtRepository.saveAll(issuedJwts.get());
    }
}
