package com.juanapi.payrollms.controller;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.juanapi.payrollms.dto.AuthRequest;
import com.juanapi.payrollms.dto.AuthResponse;
import com.juanapi.payrollms.model.User;
import com.juanapi.payrollms.service.JwtService;
import com.juanapi.payrollms.service.UserService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager autenticador;
    private final JwtService jwtService;
    private final UserService userService;

    public AuthController(AuthenticationManager autenticador, JwtService jwtService, UserService userService) {
        this.autenticador = autenticador;
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {

        autenticador.authenticate(
            new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        User user = userService.findByUsername(request.getUsername());
        
        String token = jwtService.generateToken(request.getUsername(), Map.of("roles", user.getRoles()));

        return ResponseEntity.ok(new AuthResponse(token));
    
    }
}
