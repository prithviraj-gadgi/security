package com.security.utility;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.xml.bind.DatatypeConverter;
import java.util.Base64;
import java.util.Date;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Component;

@Component
public class JWTUtil {

    private static final String SECRET_KEY =
            DatatypeConverter.printHexBinary(Jwts.SIG.HS256.key().build().getEncoded());
    private final Set<String> blacklistedTokens = ConcurrentHashMap.newKeySet();

    private SecretKey signingKey() {
        byte[] decodedKey = Base64.getDecoder().decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(decodedKey);
    }

    // Generate Token
    public String generateToken(String username) {
        return Jwts.builder()
                .id(UUID.randomUUID().toString())
                .subject(username)
                .issuer("Demo SpringBoot Application")
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(10)))
                .signWith(signingKey())
                .compact();
    }

    // Read Token
    public Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(signingKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // Read subject/username
    public String getUsername(String token) {
        return getClaims(token).getSubject();
    }

    // Get Exp date
    public Date getExpDate(String token) {
        return getClaims(token).getExpiration();
    }

    // Validate Exp date of the token
    public boolean isTokenExpired(String token) {
        return getExpDate(token).before(new Date(System.currentTimeMillis()));
    }

    // Blacklist token after logout
    public void blacklistToken(String token) {
        blacklistedTokens.add(token);
    }

    // Check if token is blacklisted
    public boolean isTokenBlacklisted(String token) {
        return blacklistedTokens.contains(token);
    }

    // Validate username passed in token and username from database, and also token expDate
    public boolean validateToken(String token, String username) {
        String tokenUserName = getUsername(token);
        return (username.equals(tokenUserName) && !isTokenExpired(token) && !isTokenBlacklisted(token));
    }
}
