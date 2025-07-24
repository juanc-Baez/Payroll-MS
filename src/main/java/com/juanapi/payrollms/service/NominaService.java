package com.juanapi.payrollms.service;


import com.juanapi.payrollms.exception.ResourceNotFoundException;
import com.juanapi.payrollms.model.Empleado;
import com.juanapi.payrollms.model.Nomina;
import com.juanapi.payrollms.model.Recibo;
import com.juanapi.payrollms.repository.EmpleadoRepo;
import com.juanapi.payrollms.repository.NominaRepo;
import com.juanapi.payrollms.repository.ReciboRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class NominaService {


    private final EmpleadoRepo empleadoRepo;
    private final NominaRepo nominaRepo;
    private final ReciboRepo reciboRepo;

    @Autowired
    public NominaService(EmpleadoRepo empleadoRepo, NominaRepo nominaRepo, ReciboRepo reciboRepo) {
        this.empleadoRepo = empleadoRepo;
        this.nominaRepo = nominaRepo;
        this.reciboRepo = reciboRepo;
    }


    public Nomina generarNomina() {

        Nomina nomina = new Nomina();
        nomina.setFechaGeneracion(LocalDate.now());

        List<Empleado> empleados = empleadoRepo.findAll();
        List<Recibo> recibos = new ArrayList<>();

        for (Empleado empleado : empleados) {
            Recibo recibo = new Recibo();
            recibo.setEmpleado(empleado);
            recibo.setNomina(nomina);

            double deducciones = calcDeducciones(empleado);
            double impuestos = calcImpuestos(empleado);
            double salarioTotal = empleado.getSalarioBase() - deducciones - impuestos;

            recibo.setDeducciones(deducciones);
            recibo.setImpuestos(impuestos);
            recibo.setSalarioTotal(salarioTotal);
            recibo.setFecha(LocalDate.now());

            recibos.add(recibo);
        }
        nomina.setRecibosPago(recibos);
        nominaRepo.save(nomina);
        reciboRepo.saveAll(recibos);
        return nomina;
    }

    private double calcImpuestos(Empleado empleado) {
        double impuestos = 0;
        // Impuestos sobre la Renta (Ejemplo: 15% del salario base) 
        impuestos += empleado.getSalarioBase() * 0.15;
        return impuestos;
    }

    private double calcDeducciones(Empleado empleado) {
        double deducciones = 0;

        // Contribuciones a la Seguridad Social (Ejemplo: 10% del salario base)
        deducciones += empleado.getSalarioBase() * 0.10;

        // Aportes a Planes de Pensiones (Ejemplo: 5% del salario base)
        deducciones += empleado.getSalarioBase() * 0.05;

        // Seguros (Ejemplo: monto fijo o porcentaje del salario base)
        deducciones += 100; // Ejemplo de monto fijo

        // Préstamos o Adelantos
        deducciones += empleado.getDeducciones(); // Suponiendo que este campo almacena deducciones adicionales

        // Contribuciones Sindicales (Ejemplo: 2% del salario base)
        deducciones += empleado.getSalarioBase() * 0.02;

        return deducciones;
    }

    public Nomina obtenerNominaPorId(Long id) throws ResourceNotFoundException {
        Optional<Nomina> nomina = nominaRepo.findById(id);
        if (nomina.isPresent()) {
            return nomina.get();
        } else {
            throw new ResourceNotFoundException("Nómina no encontrada con ID: " + id);
        }
    }

}
