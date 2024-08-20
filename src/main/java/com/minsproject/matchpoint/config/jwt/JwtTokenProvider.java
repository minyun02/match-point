package com.minsproject.matchpoint.config.jwt;

import com.minsproject.matchpoint.dto.UserDTO;
import com.minsproject.matchpoint.entity.UserToken;
import com.minsproject.matchpoint.service.TokenService;
import com.minsproject.matchpoint.service.UserService;
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

    private final TokenService tokenService;
    private final UserService userService;

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

        Claims claims = Jwts.claims();
        claims.put(CLAIMS_EMAIL, oAuth2User.getAttributes().get(CLAIMS_EMAIL));
        claims.put(CLAIMS_PROVIDER, ((OAuth2AuthenticationToken) authentication).getAuthorizedClientRegistrationId());

        return BEARER_PREFIX + Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiredTime))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getKey() {
        byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateNewAccessToken(String token) {
        UserToken userToken = tokenService.getTokenByAccessToken(token);

        if (validateToken(userToken.getRefreshToken())) {
            String email = getClaimsEmail(token);
            String provider = getClaimsProvider(token);
            UserDTO user = userService.loadUserByEmailAndProvider(email, provider);
            String newAccessToken = generateAccessToken(getAuthentication(token, user));
            tokenService.updateAccessToken(newAccessToken, email, provider);

            return newAccessToken;
        }

        return "";
    }

    public Authentication getAuthentication(String token, UserDTO user) {
        return new UsernamePasswordAuthenticationToken(user, token, user.getAuthorities());
    }
}
