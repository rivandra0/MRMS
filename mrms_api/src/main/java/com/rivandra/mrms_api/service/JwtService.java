package com.rivandra.mrms_api.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import model.User;

import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtService {

    // Replace this with a secure key in a real application, ideally fetched from environment variables
    public static final String SECRET = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";

    // Generate token with given user name
    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getRole());
        claims.put("email", user.getEmail());
        claims.put("role", user.getRole());
        
        return Jwts.builder()
            .setClaims(claims)
            .setSubject(user.getUserId())
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30)) // Token valid for 30 minutes
            .signWith(getSignKey(), SignatureAlgorithm.HS256)
            .compact();
    }
    
    // Generic method to extract any claim by name
    public <T> T extractClaim(String token, String claimName, Class<T> claimType) {
        Claims claims = extractAllClaims(token);
        return claims.get(claimName, claimType);
    }

    public String extractClaimFromRequest(HttpServletRequest request, String claimName) {
        Claims claims = extractAllClaims(extractJwtFromRequest(request));
        return claims.get(claimName, String.class);
    }

    // Validate the token against user details and expiration
    public boolean validateToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                                .setSigningKey(getSignKey())
                                .build()
                                .parseClaimsJws(token)
                                .getBody();

            // Example: Check if a specific claim exists and is valid
            String userId = claims.getSubject(); // "sub" claim
            Boolean isExpiryOk = claims.getExpiration().after(new Date());

            return (userId != null && isExpiryOk);
        } catch (JwtException e) {
            System.err.println("Invalid token: " + e.getMessage());
            return false;
        }
    }

    public String extractClaimSubject(HttpServletRequest request) {
        String jwt = extractJwtFromRequest(request);
        Claims claims = Jwts.parserBuilder()
            .setSigningKey(getSignKey())
            .build()
            .parseClaimsJws(jwt)
            .getBody();
        return claims.getSubject();
    }

    private String extractJwtFromRequest(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("jwtToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }

        final String authHeader = request.getHeader("Authorization");
        if(authHeader != null) {
            return authHeader.substring(7);
        } 
        return null;
    }

    //#region helpers

    // Get the signing key for JWT token
    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    // Extract all claims from the token
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    //#endregion
    
}