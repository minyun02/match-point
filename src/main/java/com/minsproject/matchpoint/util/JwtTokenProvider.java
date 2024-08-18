package com.minsproject.matchpoint.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Slf4j
@Component
public class JwtTokenProvider {

    @Value("${jwt.secret-key}")
    private String key;

    @Value("${jwt.token.expired-time-ms}")
    private Long expiredTimeMs;

    private static final String BEARER_PREFIX = "Bearer= ";

    public String getUserEmail(String token) {
        return extractClaims(token).get("email", String.class);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getKey()).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.info("Invalid Token", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired Token", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported Token", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty", e);
        }
        return false;
    }

    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String generateToken(Authentication authentication) {
        DefaultOAuth2User oAuth2User = (DefaultOAuth2User) authentication.getPrincipal();

        Claims claims = Jwts.claims();
        claims.put("email", oAuth2User.getAttributes().get("email"));
        claims.put("provider", ((OAuth2AuthenticationToken) authentication).getAuthorizedClientRegistrationId());

        return BEARER_PREFIX + Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiredTimeMs))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getKey() {
        byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
