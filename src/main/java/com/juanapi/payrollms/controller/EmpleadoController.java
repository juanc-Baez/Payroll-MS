package com.juanapi.payrollms.controller;


import com.juanapi.payrollms.exception.ResourceNotFoundException;
import com.juanapi.payrollms.model.Empleado;
import com.juanapi.payrollms.service.EmpleadoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/empleados")
@CrossOrigin(origins = "http://localhost:3000")
public class EmpleadoController {


    private final EmpleadoService empleadoService;

    @Autowired
    public EmpleadoController(EmpleadoService empleadoService) {
        this.empleadoService = empleadoService;
    }

    //POST CREAR EMPLEADO
    @PostMapping(value = "/crear")
    public ResponseEntity<Empleado> crearEmpleado(@RequestBody Empleado empleado) {
        Empleado nuevoEmpleado = empleadoService.crearEmpleado(empleado);

        return ResponseEntity.ok(nuevoEmpleado);
    }

    //GET CONSULTA EMPLEADOS
    @GetMapping(value = "/{id}")
    public ResponseEntity<Empleado> obtenerEmpleadoPorId(@PathVariable Long id) throws ResourceNotFoundException {
        Empleado empleado = empleadoService.obtenerEmpleadoPorId(id);
        return ResponseEntity.ok(empleado);
    }

    @GetMapping(value = "/apellido")
    public ResponseEntity<Empleado> obtenerEmpleadoPorApellido(@RequestParam String apellido) throws ResourceNotFoundException {
        Empleado empleado = empleadoService.obtenerEmpleadoPorApellido(apellido);
        return ResponseEntity.ok(empleado);
    }

    @GetMapping(value = "/obtEmpleados")
    public ResponseEntity<List<Empleado>> obtenerEmpleados() {
        List<Empleado> empleados = empleadoService.obtenerEmpleados();
        return ResponseEntity.ok(empleados);
    }


    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Empleado> elimEmpleado(@PathVariable Long id) throws ResourceNotFoundException {
        empleadoService.eliminarEmpleado(id);
        return ResponseEntity.noContent().build();
    }





























    @GetMapping(value = "/error")
    public ResponseEntity<Empleado> pruebaError() {
        throw new ResourceNotFoundException("Empleado no encontrado");
    }


}
