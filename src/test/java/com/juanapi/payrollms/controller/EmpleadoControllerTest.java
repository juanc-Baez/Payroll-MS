package com.juanapi.payrollms.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.juanapi.payrollms.model.Empleado;
import com.juanapi.payrollms.model.enums.EstadoEmpleado;
import com.juanapi.payrollms.model.enums.TipoContrato;
import com.juanapi.payrollms.service.EmpleadoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class EmpleadoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmpleadoService service;

    @Test
    void testCrarEmpleado() throws Exception {
        Empleado empleado = new Empleado();
        empleado.setNombre("Juan");
        empleado.setApellido("Baez");
        empleado.setEmail("juan.baez@gmail.com");
        empleado.setTelefono("123456789");
        empleado.setFechaContratacion(LocalDate.of(2024, 4, 9));
        empleado.setSalarioBase(2352);
        empleado.setEstado(EstadoEmpleado.ACTIVO);
        empleado.setTipoContrato(TipoContrato.PERMANENTE);

        mockMvc.perform(post("/empleados/crear")
                .contentType("application/json")
                .content(new ObjectMapper().writeValueAsString(empleado)))
                .andExpect(status().isOk());

    }



}
