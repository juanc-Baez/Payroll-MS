package com.juanapi.payrollms.controller;


import com.juanapi.payrollms.exception.ResourceNotFoundException;
import com.juanapi.payrollms.model.Empleado;
import com.juanapi.payrollms.service.EmpleadoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/empleados")
@CrossOrigin(origins = "http://localhost:3000")
@Tag(name = "Empleados", description = "API para la gestión de empleados")
public class EmpleadoController {


    private final EmpleadoService empleadoService;

    @Autowired
    public EmpleadoController(EmpleadoService empleadoService) {
        this.empleadoService = empleadoService;
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('HR')")
    @Operation(summary = "Crear un nuevo empleado", description = "Crea un nuevo empleado en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Empleado creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos del empleado inválidos")
    })
    @PostMapping(value = "/crear")
    public ResponseEntity<Empleado> crearEmpleado(@RequestBody Empleado empleado) {
        Empleado nuevoEmpleado = empleadoService.crearEmpleado(empleado);

        return ResponseEntity.ok(nuevoEmpleado);
    }

    //GET CONSULTA EMPLEADOS
    @Operation(summary = "Obtener empleado por ID", description = "Busca un empleado específico por su ID único")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Empleado encontrado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Empleado no encontrado")
    })
    @GetMapping(value = "/{id}")
    public ResponseEntity<Empleado> obtenerEmpleadoPorId(@PathVariable Long id) throws ResourceNotFoundException {
        Empleado empleado = empleadoService.obtenerEmpleadoPorId(id);
        return ResponseEntity.ok(empleado);
    }

    @Operation(summary = "Buscar empleado por apellido", description = "Busca un empleado específico utilizando su apellido")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Empleado encontrado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Empleado no encontrado")
    })
    @GetMapping(value = "/apellido")
    public ResponseEntity<Empleado> obtenerEmpleadoPorApellido(@RequestParam String apellido) throws ResourceNotFoundException {
        Empleado empleado = empleadoService.obtenerEmpleadoPorApellido(apellido);
        return ResponseEntity.ok(empleado);
    }
    
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR')")
    @Operation(summary = "Obtener todos los empleados", description = "Retorna una lista con todos los empleados registrados en el sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de empleados obtenida exitosamente"),
        @ApiResponse(responseCode = "204", description = "No hay empleados registrados")
    })
    @GetMapping(value = "/obtEmpleados")
    public ResponseEntity<List<Empleado>> obtenerEmpleados() {
        List<Empleado> empleados = empleadoService.obtenerEmpleados();
        return ResponseEntity.ok(empleados);
    }

    //Eliminar empleado
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR')")
    @Operation(summary = "Eliminar empleado", description = "Elimina un empleado del sistema utilizando su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Empleado eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Empleado no encontrado")
    })
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Empleado> elimEmpleado(@PathVariable Long id) throws ResourceNotFoundException {
        empleadoService.eliminarEmpleado(id);
        return ResponseEntity.noContent().build();
    }

    //Actualizar empleado
    @Operation(summary = "Actualizar empleado", description = "Actualiza la información de un empleado existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Empleado actualizado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Empleado no encontrado"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PutMapping(value = "/actualizar/{id}")
    public ResponseEntity<Empleado> actualizarEmpleado(@PathVariable Long id, @RequestBody Empleado empleadoDetalles) throws ResourceNotFoundException {
        Empleado empleadoAct = empleadoService.actualizarEmpleado(id, empleadoDetalles);
        return ResponseEntity.ok(empleadoAct);
    }

    @Operation(summary = "Endpoint de prueba de error", description = "Endpoint utilizado para probar el manejo de excepciones")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "404", description = "Error simulado - Empleado no encontrado")
    })
    @GetMapping(value = "/error")
    public ResponseEntity<Empleado> pruebaError() {
        throw new ResourceNotFoundException("Empleado no encontrado");
    }


}
