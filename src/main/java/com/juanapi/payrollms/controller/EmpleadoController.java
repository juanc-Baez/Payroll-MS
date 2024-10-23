package com.juanapi.payrollms.controller;


import com.juanapi.payrollms.exception.ResourceNotFoundException;
import com.juanapi.payrollms.model.Empleado;
import com.juanapi.payrollms.service.EmpleadoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/empleados")
@CrossOrigin(origins = "http://localhost:3000")
public class EmpleadoController {


    private final EmpleadoService empleadoService;

    @Autowired
    public EmpleadoController(EmpleadoService empleadoService) {
        this.empleadoService = empleadoService;
    }

    @PostMapping(value = "/crear")
    public ResponseEntity<Empleado> crearEmpleado(@RequestBody Empleado empleado) {
        Empleado nuevoEmpleado = empleadoService.crearEmpleado(empleado);

        return ResponseEntity.ok(nuevoEmpleado);
    }



    @GetMapping(value = "/error")
    public ResponseEntity<Empleado> pruebaError() {
        throw new ResourceNotFoundException("Empleado no encontrado");
    }


}
