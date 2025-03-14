package com.travishuy.restaurant_manager.restaurant_manager.jwt;

import com.travishuy.restaurant_manager.restaurant_manager.model.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * A utility class that provides methods for generating and validating JWT tokens.
 *
 * @version 0.1
 * @since 03-02-2025
 * @author TravisHuy
 */
@Slf4j
@Component
public class JwtTokenProvider {

    /** Secret key used to sign the JWT token */
    @Value("${jwt.secret}")
    private String jwtSecret;
    /** Expiration time of the JWT token in milliseconds */
    @Value("${jwt.expiration}")
    private int jwtExpirationInMs;

    /** Expiration time of the refresh token in milliseconds */
    @Value("${jwt.refreshExpiration}")
    private int jwtRefreshExpirationInMs;

    /** Secret key used to sign the JWT token */
    private SecretKey secretKey;

    private Set<String> tokenBlacklist = Collections.synchronizedSet(new HashSet<>());


    @PostConstruct
    public void init(){
        this.secretKey = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        log.info("JWT Provider initialized with secret key length: "+ jwtSecret.length());
    }

    /**
     * Generates a JWT token for the given user.
     *
     * @param user the user to generate the token for
     * @return the generated JWT token
     */
    public String generateToken(User user) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

        return Jwts.builder()
                .setSubject(user.getId())
                .claim("email", user.getEmail())
                .claim("role", user.getRole())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(secretKey,SignatureAlgorithm.HS512)
                .compact();
    }
    /**
     * Generates a refresh token for the given user.
     *
     * @param user the user to generate the refresh token for
     * @return the generated refresh token
     */
    public String generateRefeshToken(User user) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtRefreshExpirationInMs);

        return Jwts.builder()
                .setSubject(user.getId())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();
    }
    /**
     * Extracts the user ID from the JWT token.
     *
     * @param token the JWT token
     * @return the user ID extracted from the token
     */
    public String getUserIdFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }
    /**
     * Validates the JWT token.
     *
     * @param authToken the JWT token to validate
     * @return true if the token is valid, false otherwise
     */
    public boolean validateToken(String authToken) {
        try {
            if(isTokenBlacklisted(authToken)){
                return false;
            }
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(authToken);
            return true;

        } catch (SignatureException ex) {
            log.error("Invalid JWT signature: {}", ex.getMessage());
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token: {}", ex.getMessage());
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token: {}", ex.getMessage());
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token: {}", ex.getMessage());
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty: {}", ex.getMessage());
        }
        return false;
    }

    /**
     * Invalidates a JWT token by adding it to the blacklist.
     *
     * @param token The token to invalidate
     */
    public void invalidateToken(String token) {
        tokenBlacklist.add(token);
    }

    /**
     * Checks if a token is blacklisted (has been invalidated).
     *
     * @param token The token to check
     * @return true if the token is blacklisted, false otherwise
     */
    public boolean isTokenBlacklisted(String token) {
        return tokenBlacklist.contains(token);
    }
}
