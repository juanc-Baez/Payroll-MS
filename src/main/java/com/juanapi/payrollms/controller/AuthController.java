package com.juanapi.payrollms.controller;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.juanapi.payrollms.dto.AuthRequest;
import com.juanapi.payrollms.dto.AuthResponse;
import com.juanapi.payrollms.dto.RegisterRequest;
import com.juanapi.payrollms.service.JwtService;
import com.juanapi.payrollms.service.UserService;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.security.access.prepost.PreAuthorize;

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

        UserDetails user = userService.findByUsername(request.getUsername());

        // Asegura que los roles en el JWT tengan el prefijo 'ROLE_'
        var roles = user.getAuthorities().stream()
            .map(authority -> {
                String auth = authority.getAuthority();
                return auth.startsWith("ROLE_") ? auth : "ROLE_" + auth;
            })
            .toList();

        var claims = new HashMap<String,Object>();
        claims.put("roles", roles);
        String token = jwtService.generateToken(user, claims);

        return ResponseEntity.ok(new AuthResponse(token));
    }

    @PostMapping("/register")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        var roles = request.getRoles() == null || request.getRoles().isEmpty()
            ? java.util.Set.of("EMPLEADO")
            : request.getRoles();
        userService.createUser(request.getUsername(), request.getPassword(), roles);
        UserDetails user = userService.findByUsername(request.getUsername());
        var roleList = user.getAuthorities().stream().map(a -> a.getAuthority()).toList();
        String token = jwtService.generateToken(user, java.util.Map.of("roles", roleList));
        return ResponseEntity.ok(new AuthResponse(token));
    }
}
