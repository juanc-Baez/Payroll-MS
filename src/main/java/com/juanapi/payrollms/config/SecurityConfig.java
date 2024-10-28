package com.juanapi.payrollms.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class SecurityConfig {


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Página personalizada de login (ajusta la URL si es necesario)
        http.
                authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/empleado/**").hasRole("EMPLEADO")
                        .requestMatchers("/supervisor/**").hasRole("SUPERVISOR")
                        .anyRequest().authenticated()
                )
                .formLogin(AbstractAuthenticationFilterConfigurer::permitAll         // Permitir el acceso a todos a la página de login
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")         // URL personalizada para el logout
                        .logoutSuccessUrl("/login")   // Redirige a la página de login tras el logout
                        .permitAll()
                );
        return http.build();
    }
}

