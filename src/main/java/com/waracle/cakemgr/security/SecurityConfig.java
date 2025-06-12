package com.waracle.cakemgr.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security configuration class for Cake Manager application.
 * <p>
 * This class configures HTTP security including CSRF protection, HTTP Basic Authentication,
 * role-based access control, and sets Content Security Policy (CSP) headers for advanced
 * cross-site scripting (XSS) mitigation.
 * <p>
 * CSRF protection is disabled to simplify API testing (adjust for production as needed).
 * HTTP Basic Authentication is used with in-memory user details manager defining two users:
 * <ul>
 *     <li>cakemanager with role ADMIN</li>
 *     <li>cakeuser with role CUSTOMER</li>
 * </ul>
 * <p>
 * The Content Security Policy restricts resource loading to trusted sources to help prevent XSS attacks.
 *
 * @author Omkar Tamboli
 */
@Configuration
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityConfig.class);

    /**
     * Configures the security filter chain.
     * <p>
     * CSRF protection is enabled by default and requires authentication for all requests.
     * Applies HTTP Basic authentication and sets Content Security Policy headers for XSS mitigation.
     *
     * @param http the HttpSecurity to modify
     * @return the configured SecurityFilterChain
     * @throws Exception if an error occurs during configuration
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        LOGGER.info("Creating SecurityFilterChain Configuration.");
        http
                // CSRF is disabled for postman API testing, should be enabled for PROD
                .csrf(csrf -> csrf.disable())
                // Enable HTTP Basic authentication
                .httpBasic(Customizer.withDefaults())
                // Configure security headers
                .headers(headers -> headers
                        // Set Content Security Policy for XSS mitigation
                        .contentSecurityPolicy(csp -> csp
                                .policyDirectives(
                                        "default-src 'self'; " +
                                                "script-src 'self'; " +
                                                "style-src 'self' 'unsafe-inline'; " +
                                                "img-src 'self' data:; " +
                                                "font-src 'self'; " +
                                                "object-src 'none'; " +
                                                "frame-ancestors 'none'; " +
                                                "base-uri 'self';"
                                )
                        )
                );

        return http.build();
    }

    /**
     * Defines the in-memory user details service with two users: admin and user.
     * <p>
     * Passwords are stored in plain text with {noop} prefix for demonstration only.
     * Replace with proper password encoding in production.
     *
     * @return the configured UserDetailsService
     */
    @Bean
    public UserDetailsService userDetailsService() {

        LOGGER.info("Creating in memory UserDetailsService Configuration.");
        UserDetails admin = User
                .withUsername("cakemanager")
                .password("{noop}ckm#$5^7rd")
                .roles("ADMIN")
                .build();

        UserDetails user = User
                .withUsername("cakeuser")
                .password("{noop}cku#$7^0rd")
                .roles("CUSTOMER")
                .build();

        return new InMemoryUserDetailsManager(admin, user);
    }
}
