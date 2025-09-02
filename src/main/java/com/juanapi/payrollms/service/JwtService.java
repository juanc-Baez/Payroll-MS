
package com.juanapi.payrollms.service;

import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

@Service
public class JwtService {
    
    @Value("${jwt.secret-key}")
    private String secret; // Inyecta desde application.properties
    private static final Long EXPIRATION_MS = 86400000L; // 1 día de validez

    private SecretKey secretKey;
    // Método para inicializar la clave secreta
    // Se ejecuta una vez al iniciar la aplicación
    @PostConstruct
    public void init() {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
    }
    private SecretKey getSecretKey() {
        return secretKey;
    }

    public String generateToken(UserDetails user, Map<String, Object> extraClaims) {

        Date expirationDate = new Date(System.currentTimeMillis() + EXPIRATION_MS);
        extraClaims.put("roles", user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));
        return Jwts.builder()
                .claims(extraClaims)
                .subject(user.getUsername())
                .issuedAt(new Date())
                .signWith(getSecretKey())
                .expiration(expirationDate)
                .compact(); 
    }

    // Método para validar un JWT
    public boolean validateToken(String token, String username) {
        try { 
            Jws<Claims> jws = Jwts.parser()
                    .verifyWith(getSecretKey())
                    .build()
                    .parseSignedClaims(token);

            String tokenUsername = jws.getPayload().getSubject(); // Extrae el subject del token
            Date expiration = jws.getPayload().getExpiration(); // Extrae la fecha de expiración

            return tokenUsername.equals(username) && !expiration.before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            System.out.println("Token inválido: " + e.getMessage());
            return false;
        }
    }

    // Método para extraer el username del token
    public String extractUsername(String token) {
        try {
            Jws<Claims> jws = Jwts.parser()
                    .verifyWith(getSecretKey())
                    .build()
                    .parseSignedClaims(token);
            return jws.getPayload().getSubject(); // Extrae el subject(username) del token
        } catch (JwtException | IllegalArgumentException e) {
            return null;
        }
    }

        // Extraer roles del token JWT
    public java.util.List<String> extractRoles(String token) {
        try {
            Jws<Claims> jws = Jwts.parser()
                    .verifyWith(getSecretKey())
                    .build()
                    .parseSignedClaims(token);
            Object rolesObj = jws.getPayload().get("roles");
            if (rolesObj instanceof java.util.List<?>) {
                // Convertir a lista de strings
                return ((java.util.List<?>) rolesObj).stream()
                    .map(Object::toString)
                    .toList();
            }
            return java.util.Collections.emptyList();
        } catch (JwtException | IllegalArgumentException e) {
            return java.util.Collections.emptyList();
        }
    }
}
