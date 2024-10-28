package com.juanapi.payrollms.repository;


import com.juanapi.payrollms.model.Nomina;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NominaRepo extends JpaRepository<Nomina, Long> {

}
