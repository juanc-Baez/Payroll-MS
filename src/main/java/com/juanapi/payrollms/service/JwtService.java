package com.juanapi.payrollms.service;

import java.util.Date;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
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

    public String generateToken(String username, Map<String, Object> extraClaims) {

        Date expirationDate = new Date(System.currentTimeMillis() + EXPIRATION_MS);
        return Jwts.builder()
                .claims(extraClaims)
                .subject(username)
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
}
