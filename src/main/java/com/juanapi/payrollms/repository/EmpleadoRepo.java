package com.juanapi.payrollms.repository;


import com.juanapi.payrollms.model.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmpleadoRepo extends JpaRepository<Empleado, Long> {

    Optional<Empleado> findByApellido(String apellido);

    Optional<Empleado> findByUserUsername(String username);

}
