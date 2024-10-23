package com.juanapi.payrollms.service;


import com.juanapi.payrollms.model.Empleado;
import com.juanapi.payrollms.repository.EmpleadoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmpleadoService {


    private EmpleadoRepo empleadoRepo;

    @Autowired
    public EmpleadoService(EmpleadoRepo empleadoRepo) {
        this.empleadoRepo = empleadoRepo;
    }

    public Empleado crearEmpleado(Empleado empleado) {
        return empleadoRepo.save(empleado);
    }
}
