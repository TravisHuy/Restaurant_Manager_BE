package com.travishuy.restaurant_manager.restaurant_manager.jwt;

import com.travishuy.restaurant_manager.restaurant_manager.model.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
/**
 * A utility class that provides methods for generating and validating JWT tokens.
 *
 * @version 0.1
 * @since 03-02-2025
 * @author TravisHuy
 */
@Component
public class JwtTokenProvider {
    /** Secret key used to sign the JWT token */
    @Value("${jwt.secret}")
    private String jwtSecret;
    /** Expiration time of the JWT token in milliseconds */
    @Value("${jwt.expiration}")
    private int jwtExpirationInMs;

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
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, Keys.hmacShaKeyFor(jwtSecret.getBytes()))
                .compact();
    }
    /**
     * Extracts the user ID from the JWT token.
     *
     * @param token the JWT token
     * @return the user ID extracted from the token
     */
    public String getUserIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
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
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException ex) {
            // Invalid JWT signature
            return false;
        } catch (MalformedJwtException ex) {
            // Invalid JWT token
            return false;
        } catch (ExpiredJwtException ex) {
            // Expired JWT token
            return false;
        } catch (UnsupportedJwtException ex) {
            // Unsupported JWT token
            return false;
        } catch (IllegalArgumentException ex) {
            // JWT claims string is empty
            return false;
        }
    }
}
