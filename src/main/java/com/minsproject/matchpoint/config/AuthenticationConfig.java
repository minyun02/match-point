package com.minsproject.matchpoint.config;

import com.minsproject.matchpoint.config.jwt.JwtTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class AuthenticationConfig {

    private final JwtTokenFilter jwtTokenFilter;
    private final CustomOAuth2UserService customOAuth2UserService;

    @Bean
    public WebSecurityCustomizer configure() {
        return (web -> web.ignoring()
                .requestMatchers("h2-console/**", "/error", "/favicon.ico")
        );
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .headers(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable)
            .formLogin(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(authorize -> authorize
                    .requestMatchers(PathRequest.toH2Console()).permitAll()
                    .requestMatchers("/api/*/auth/refresh", "/api/*/auth/token", "/api/*/users/signup", "/api/*/users/login", "/api/*/users/token", "/api/*/users/provider", "/api/*/profiles/nickname").permitAll()
                    .requestMatchers("/swagger", "/swagger-ui.html", "/swagger-ui/**", "/api-docs", "/api-docs/**", "/v3/api-docs/**").permitAll()
                    .anyRequest().authenticated()
            )
            .logout(logout -> logout
                    .logoutSuccessUrl("/")
                    .invalidateHttpSession(true)
            )
            .sessionManagement(session -> session
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
        ;

        return http.build();
    }
}
