package com.juanapi.payrollms.controller;

import com.juanapi.payrollms.exception.ResourceNotFoundException;
import com.juanapi.payrollms.model.Empleado;
import com.juanapi.payrollms.model.Recibo;
import com.juanapi.payrollms.service.DocumentService;
import com.juanapi.payrollms.service.EmpleadoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/recibos")
@CrossOrigin(origins = "http://localhost:3000")
@Tag(name = "Recibos", description = "API para la gestión de recibos de pago individuales")
public class ReciboController {

    private final DocumentService documentService;
    private final EmpleadoService empleadoService;

    public ReciboController(DocumentService documentService, EmpleadoService empleadoService) {
        this.documentService = documentService;
        this.empleadoService = empleadoService;
    }

    @Operation(summary = "Generar recibo individual", description = "Genera un recibo de pago individual para un empleado específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Recibo generado y descargado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Empleado no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error al generar el recibo")
    })
    @GetMapping(value = "/empleado/{empleadoId}/pdf")
    public ResponseEntity<byte[]> generarReciboEmpleado(@PathVariable Long empleadoId) {
        try {
            Empleado empleado = empleadoService.obtenerEmpleadoPorId(empleadoId);
            
            // Crear un recibo temporal para el empleado
            Recibo recibo = new Recibo();
            recibo.setEmpleado(empleado);
            recibo.setFecha(LocalDate.now());
            
            // Calcular valores (usando la misma lógica del servicio de nómina)
            double deducciones = calcularDeducciones(empleado);
            double impuestos = calcularImpuestos(empleado);
            double salarioTotal = empleado.getSalarioBase() - deducciones - impuestos;
            
            recibo.setDeducciones(deducciones);
            recibo.setImpuestos(impuestos);
            recibo.setSalarioTotal(salarioTotal);
            
            byte[] pdfBytes = documentService.generarReciboPDF(recibo);
            
            String filename = "recibo_" + empleado.getApellido().toLowerCase() + "_" 
                            + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + ".pdf";
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", filename);
            headers.setContentLength(pdfBytes.length);
            
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdfBytes);
                    
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    private double calcularImpuestos(Empleado empleado) {
        // Impuestos sobre la Renta (Ejemplo: 15% del salario base) 
        return empleado.getSalarioBase() * 0.15;
    }

    private double calcularDeducciones(Empleado empleado) {
        double deducciones = 0;

        // Contribuciones a la Seguridad Social (Ejemplo: 10% del salario base)
        deducciones += empleado.getSalarioBase() * 0.10;

        // Aportes a Planes de Pensiones (Ejemplo: 5% del salario base)
        deducciones += empleado.getSalarioBase() * 0.05;

        // Seguros (Ejemplo: monto fijo)
        deducciones += 100;

        // Préstamos o Adelantos
        deducciones += empleado.getDeducciones();

        // Contribuciones Sindicales (Ejemplo: 2% del salario base)
        deducciones += empleado.getSalarioBase() * 0.02;

        return deducciones;
    }
}
