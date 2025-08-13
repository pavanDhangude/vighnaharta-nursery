package com.vighnaharta.nursery.config;

import com.vighnaharta.nursery.jwt.JwtAuthenticationFilter;
import com.vighnaharta.nursery.service.CustomUserDetailsService;
import com.vighnaharta.nursery.jwt.JwtService;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;

import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;
    private final JwtService jwtService;

    @Bean
    public JwtAuthenticationFilter jwtAuthFilter() {
        return new JwtAuthenticationFilter(jwtService, customUserDetailsService);
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**", "/uploads/**").permitAll()
                // ✅ Booking create - User & Admin both
                .requestMatchers(HttpMethod.POST, "/api/bookings/**").hasAnyRole("USER", "ADMIN") 
             // ✅ My bookings - User & Admin both
                .requestMatchers(HttpMethod.GET, "/api/bookings/my-bookings").hasAnyRole("USER", "ADMIN")
             // ✅ Get all bookings - Admin only
                .requestMatchers(HttpMethod.GET, "/api/bookings/**").hasRole("ADMIN") 
                // ✅ Update booking - Admin only  
                .requestMatchers(HttpMethod.PUT, "/api/bookings/**").hasRole("ADMIN")  
             // ✅ Delete booking - Admin only
                .requestMatchers(HttpMethod.DELETE, "/api/bookings/**").hasRole("ADMIN")               // Only Admin can delete booking
                .anyRequest().authenticated()
            )
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authenticationProvider(authenticationProvider())
            .addFilterBefore(jwtAuthFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
