package com.juanapi.payrollms.service;


import com.juanapi.payrollms.exception.ResourceNotFoundException;
import com.juanapi.payrollms.model.Empleado;
import com.juanapi.payrollms.repository.EmpleadoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public Empleado obtenerEmpleadoPorId(Long id) {
        return empleadoRepo.findById(id).
                orElseThrow(() -> new ResourceNotFoundException("Empleado no encontrado con id" + id));
    }

    public Empleado obtenerEmpleadoPorApellido(String apellido) {
        return empleadoRepo.findByApellido(apellido)
                .orElseThrow(() -> new ResourceNotFoundException("Empleado no encontrado con apellido" + apellido));
    }

    public List<Empleado> obtenerEmpleados() {
        return empleadoRepo.findAll();
    }
}
