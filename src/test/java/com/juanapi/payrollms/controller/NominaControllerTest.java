package com.juanapi.payrollms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.boot.test.mock.mockito.MockBean;
import com.juanapi.payrollms.model.User;
import com.juanapi.payrollms.model.Role;
import com.juanapi.payrollms.repository.UserRepo;
import com.juanapi.payrollms.repository.RoleRepo;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.test.web.servlet.MvcResult;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
class NominaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUpUser() {
        String username = "admin";
        String password = "admin123";
        String encodedPassword = passwordEncoder.encode(password);
        Role adminRole = roleRepo.findByName("ADMIN").orElseGet(() -> {
            Role r = new Role();
            r.setName("ADMIN");
            return roleRepo.save(r);
        });
        User user = userRepo.findByUsername(username).orElseGet(() -> {
            User u = new User();
            u.setUsername(username);
            u.setPassword(encodedPassword);
            u.getRoles().add(adminRole);
            return userRepo.save(u);
        });
        // Si el usuario existe pero no tiene el rol o el password correcto, actualiza
        if (!user.getRoles().contains(adminRole)) {
            user.getRoles().add(adminRole);
            userRepo.save(user);
        }
        if (!passwordEncoder.matches(password, user.getPassword())) {
            user.setPassword(encodedPassword);
            userRepo.save(user);
        }
    }

    @Test
    @DisplayName("Debería generar una nómina correctamente (201 Created)")
    void generarNomina() throws Exception {
        // 1. Realizar login para obtener el JWT
        String loginPayload = "{" +
                "\"username\": \"admin\"," +
                "\"password\": \"admin123\"}";

        MvcResult loginResult = mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginPayload))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = loginResult.getResponse().getContentAsString();
        // Ajusta el nombre del campo si tu respuesta JWT es diferente
        String token = objectMapper.readTree(responseBody).get("token").asText();
        assertThat(token).isNotBlank();

        // 2. Llamar al endpoint protegido con el JWT
        String payload = "{}"; // Ajusta el payload según tu modelo
        ResultActions result = mockMvc.perform(post("/nominas/generar")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .content(payload));
        result.andExpect(status().isCreated()); // O el status que corresponda
    }
}
