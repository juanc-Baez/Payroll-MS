package com.juanapi.payrollms.service;


import com.juanapi.payrollms.exception.ResourceNotFoundException;
import com.juanapi.payrollms.model.Empleado;
import com.juanapi.payrollms.repository.EmpleadoRepo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class EmpleadoService {


    private final EmpleadoRepo empleadoRepo;

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

    public void eliminarEmpleado(Long id) {
        Empleado empleado = empleadoRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empleado no encontrado con id" + id));
        empleadoRepo.delete(empleado);
    }


    public Empleado actualizarEmpleado(Long id, Empleado detallesEmpleado) {
        Empleado empleadoExistente = empleadoRepo.findById((id))
                .orElseThrow(() -> new ResourceNotFoundException("Empleado no encontrado con id" + id));


        //copiar las propiedades de un objeto de origen a un objeto de destino
        BeanUtils.copyProperties(detallesEmpleado, empleadoExistente, getNullPropertyNames(detallesEmpleado));

        return empleadoRepo.save(empleadoExistente);
    }


    //getNullPropertyNames es excluir las propiedades que están null en el objeto de origen
    // para que no sobreescriban valores existentes en el objeto de destino
    private String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<String>();
        for (java.beans.PropertyDescriptor pd : pds) {
            Object value = src.getPropertyValue(pd.getName());
            if (value == null) emptyNames.add(pd.getName());
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    public Empleado obtenerEmpleadoPorUsername(String username) {
        return empleadoRepo.findByUserUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Empleado no encontrado con username: " + username));   
    }




}
