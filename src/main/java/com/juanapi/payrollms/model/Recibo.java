package com.juanapi.payrollms.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


import java.time.LocalDate;

@Getter
@Setter
@Entity
public class Recibo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;


    private double salarioTotal;
    private double deducciones;
    private double impuestos;
    private LocalDate fecha;


    @ManyToOne
    @JoinColumn(name = "empleado_id", nullable = false)
    private Empleado empleado;

    @ManyToOne
    @JoinColumn(name = "nomina_id", nullable = false)
    private Nomina nomina;

}
