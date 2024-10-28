package com.juanapi.payrollms.repository;


import com.juanapi.payrollms.model.Recibo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReciboRepo extends JpaRepository<Recibo, Long> {
}
