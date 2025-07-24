package com.juanapi.payrollms.controller;

import com.juanapi.payrollms.model.Nomina;
import com.juanapi.payrollms.service.DocumentService;
import com.juanapi.payrollms.service.NominaService;
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
@RequestMapping("/nominas")
@CrossOrigin(origins = "http://localhost:3000")
@Tag(name = "Nóminas", description = "API para la gestión de nóminas del sistema de payroll")
public class NominaController {

    private final NominaService nominaService;
    private final DocumentService documentService;

    public NominaController(NominaService nominaService, DocumentService documentService) {
        this.nominaService = nominaService;
        this.documentService = documentService;
    }

    @Operation(summary = "Generar nómina", description = "Genera una nueva nómina para todos los empleados activos del sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Nómina generada exitosamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor al generar la nómina")
    })
    @PostMapping(value = "/generar")
    public ResponseEntity<Nomina> generarNomina() {
        Nomina nomina = nominaService.generarNomina();
        return ResponseEntity.ok(nomina);
    }

    @Operation(summary = "Descargar nómina en PDF", description = "Genera y descarga la nómina en formato PDF")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "PDF generado y descargado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Nómina no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error al generar el PDF")
    })
    @GetMapping(value = "/{id}/pdf")
    public ResponseEntity<byte[]> descargarNominaPDF(@PathVariable Long id) {
        try {
            Nomina nomina = nominaService.obtenerNominaPorId(id);
            byte[] pdfBytes = documentService.generarNominaPDF(nomina);
            
            String filename = "nomina_" + nomina.getFechaGeneracion().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + ".pdf";
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", filename);
            headers.setContentLength(pdfBytes.length);
            
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdfBytes);
                    
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @Operation(summary = "Generar y descargar nómina actual", description = "Genera una nueva nómina y la descarga inmediatamente en formato PDF")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Nómina generada y PDF descargado exitosamente"),
        @ApiResponse(responseCode = "500", description = "Error al generar la nómina o el PDF")
    })
    @PostMapping(value = "/generar-pdf")
    public ResponseEntity<byte[]> generarYDescargarNomina() {
        try {
            Nomina nomina = nominaService.generarNomina();
            byte[] pdfBytes = documentService.generarNominaPDF(nomina);
            
            String filename = "nomina_" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + ".pdf";
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", filename);
            headers.setContentLength(pdfBytes.length);
            
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdfBytes);
                    
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
