package com.travishuy.restaurant_manager.restaurant_manager.config;

import com.travishuy.restaurant_manager.restaurant_manager.jwt.JwtAuthenticationFilter;
import com.travishuy.restaurant_manager.restaurant_manager.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * Configures the security settings for the application.
 *
 * @version 0.1
 * @since 04-02-2025
 * @author TravisHuy
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    /** The custom user details service */
    private final CustomUserDetailsService customUserDetailsService;
    /** The JWT authentication filter */
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * Constructs a new SecurityConfig with the given parameters.
     *
     * @param customUserDetailsService the custom user details service
     * @param jwtAuthenticationFilter the JWT authentication filter
     */
    public SecurityConfig(CustomUserDetailsService customUserDetailsService, JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.customUserDetailsService = customUserDetailsService;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }
    /**
     * Configures the security filter chain for the application.
     *
     * @param http the HttpSecurity object to configure
     * @return the SecurityFilterChain object
     * @throws Exception if an error occurs while configuring the security filter chain
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/orders/**").permitAll()
                        .requestMatchers("/api/tables/add").hasAnyAuthority("ROLE_ADMIN","ROLE_MANAGER")
                        .requestMatchers("/api/tables/**").permitAll()
                        .requestMatchers("/api/floors/add").hasAnyAuthority("ROLE_ADMIN","ROLE_MANAGER")
                        .requestMatchers("/api/floors/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_MANAGER", "ROLE_EMPLOYEE")
                        .requestMatchers("/api/category/add").hasAnyAuthority("ROLE_ADMIN","ROLE_MANAGER")
                        .requestMatchers("/api/category/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_MANAGER", "ROLE_EMPLOYEE")
                        .requestMatchers("/api/menu-items/add").hasAnyAuthority("ROLE_ADMIN","ROLE_MANAGER")
                        .requestMatchers("/api/menu-items/**").permitAll()
                        .requestMatchers("/api/reservations/**").permitAll()
                        .anyRequest().authenticated()
                )
                .userDetailsService(customUserDetailsService)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
    /**
     * Creates a new PasswordEncoder bean.
     *
     * @return the PasswordEncoder bean
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    /**
     * Creates a new AuthenticationManager bean.
     *
     * @param config the AuthenticationConfiguration object
     * @return the AuthenticationManager bean
     * @throws Exception if an error occurs while creating the AuthenticationManager bean
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Creates a new CorsConfigurationSource bean.
     *
     * @return the CorsConfigurationSource bean
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of("*"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true); // Cho phép gửi cookie/token nếu cần
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
