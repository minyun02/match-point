package com.minsproject.matchpoint.config.jwt;

import com.minsproject.matchpoint.dto.request.UserRequest;
import com.minsproject.matchpoint.entity.User;
import com.minsproject.matchpoint.entity.UserToken;
import com.minsproject.matchpoint.exception.ErrorCode;
import com.minsproject.matchpoint.exception.MatchPointException;
import com.minsproject.matchpoint.service.TokenService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Component;
import org.thymeleaf.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtTokenProvider {

    @Value("${jwt.secret-key}")
    private String key;
    private static final Long EXPIRED_TIME_MS = 1000 * 60 * 30L;
    private static final Long REFRESH_TOKEN_EXPIRED_TIME_MS = 1000 * 60 * 60L * 24 * 7;
    private static final String BEARER_PREFIX = "Bearer= ";
    private static final String CLAIMS_EMAIL = "email";
    private static final String CLAIMS_PROVIDER = "provider";
    private static final String CLAIMS_PROVIDER_ID = "providerId";

    private final TokenService tokenService;

    public String getClaimsEmail(String token) {
        return extractClaims(token).get(CLAIMS_EMAIL, String.class);
    }

    public String getClaimsProvider(String token) {
        return extractClaims(token).get(CLAIMS_PROVIDER, String.class);
    }

    public boolean validateToken(String token) {
        if (StringUtils.isEmpty(token)) {
            throw new SecurityException("token is empty");
        }

        Claims claims = extractClaims(token);
        return claims.getExpiration().after(new Date());
    }

    public Claims extractClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            log.info("Expired Token", e);
            return e.getClaims();
        } catch (MalformedJwtException e) {
            log.info("Unsupported Token", e);
            throw new MalformedJwtException("invalid token = {}", e);
        } catch (SecurityException e) {
            log.info("", e);
            throw new SecurityException("invalid jwt signature = {}", e);
        }
    }

    public String generateAccessToken(Authentication authentication) {
        return generateToken(authentication, EXPIRED_TIME_MS);
    }

    public void generateRefreshToken(Authentication authentication, String accessToken) {
        String provider = ((OAuth2AuthenticationToken) authentication).getAuthorizedClientRegistrationId();
        Map<String, Object> attributes = ((DefaultOAuth2User) authentication.getPrincipal()).getAttributes();
        String refreshToken = generateToken(authentication, REFRESH_TOKEN_EXPIRED_TIME_MS);
        tokenService.saveOrUpdate((String) attributes.get(CLAIMS_EMAIL), provider, refreshToken, accessToken);
    }

    public String generateToken(Authentication authentication, long expiredTime) {
        DefaultOAuth2User oAuth2User = (DefaultOAuth2User) authentication.getPrincipal();
        String provider = ((OAuth2AuthenticationToken) authentication).getAuthorizedClientRegistrationId();

        Claims claims = Jwts.claims();
        Object email = getEmail(provider, oAuth2User.getAttributes());
        claims.put(CLAIMS_EMAIL, email);
        claims.put(CLAIMS_PROVIDER, provider);

        return generateTokenWithClaims(claims, expiredTime);
    }

    public String generateAccessTokenFromUser(User user) {
        return generateTokenFromUser(user, EXPIRED_TIME_MS);
    }

    private String generateTokenFromUser(User user, Long expiredTimeMs) {
        Claims claims = Jwts.claims();
        claims.put(CLAIMS_EMAIL, user.getEmail());
        claims.put(CLAIMS_PROVIDER, user.getProvider());
        claims.put(CLAIMS_PROVIDER_ID, user.getProviderId());

        return generateTokenWithClaims(claims, expiredTimeMs);
    }

    private String generateTokenWithClaims(Claims claims, Long expiredTimeMs) {
        return BEARER_PREFIX + Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiredTimeMs))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateNewAccessToken(String token) {
        UserToken userToken = tokenService.getTokenByAccessToken(token);

        if (validateToken(userToken.getRefreshToken())) {
            Claims claims = extractClaims(token);
            return generateTokenWithClaims(claims, EXPIRED_TIME_MS);
        }

        throw new MatchPointException(ErrorCode.INVALID_TOKEN);
    }

    private Key getKey() {
        byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Authentication getAuthentication(String token, UserRequest user) {
        return new UsernamePasswordAuthenticationToken(user, token, user.getAuthorities());
    }

    private Object getEmail(String provider, Map<String, Object> attributes) {
        switch (provider) {
            case "google" -> {
                return attributes.get("email");
            }
            case "kakao" -> {
                Map<String, Object> account = (Map<String, Object>) attributes.get("kakao_account");
                Map<String, Object> profile = (Map<String, Object>) account.get("profile");

                return (String) profile.get("nickname") + attributes.get("id");
            }
            case "naver" -> {
                Map<String, Object> response = (Map<String, Object>) attributes.get("response");
                return response.get("email");
            }
            default -> throw new MatchPointException(ErrorCode.WRONG_PROVIDER, "소셜로그인에 문제가 발생했습니다.");
        }
    }
}
