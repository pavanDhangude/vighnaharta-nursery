package com.vighnaharta.nursery.service;

import com.vighnaharta.nursery.dto.*;
import com.vighnaharta.nursery.entity.User;
import com.vighnaharta.nursery.enums.Role;
import com.vighnaharta.nursery.jwt.JwtService;
import com.vighnaharta.nursery.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuthenticationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;

    public AuthenticationResponse register(SignupRequest request) {
        log.info("Registering user: {}", request.getUsername());

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole() != null ? request.getRole() : Role.USER)
                .build();

        userRepository.save(user);

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(user.getUsername());

        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("role", user.getRole().name()); // role claim add kiya

        String jwt = jwtService.generateToken(extraClaims, userDetails);

        return new AuthenticationResponse(jwt);
    }

    public AuthenticationResponse authenticate(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(user.getUsername());

        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("role", user.getRole().name()); // role claim add kiya

        String jwt = jwtService.generateToken(extraClaims, userDetails);

        return new AuthenticationResponse(jwt);
    }
}
