package com.juanapi.payrollms.controller;


import com.juanapi.payrollms.model.Nomina;
import com.juanapi.payrollms.service.NominaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/nominas")
@CrossOrigin(origins = "http://localhost:3000")
public class NominaController {



    private final NominaService nominaService;

    public NominaController(NominaService nominaService) {
        this.nominaService = nominaService;
    }


    @PostMapping(value = "/generar")
    public ResponseEntity<Nomina> generarNomina() {
        Nomina nomina = nominaService.generarNomina();
        return ResponseEntity.ok(nomina);
    }
}
