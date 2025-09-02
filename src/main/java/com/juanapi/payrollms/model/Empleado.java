package com.juanapi.payrollms.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.juanapi.payrollms.model.enums.EstadoEmpleado;
import com.juanapi.payrollms.model.enums.TipoContrato;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
@Entity
public class Empleado {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String apellido;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String telefono;

    @Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaContratacion;

    @Column(nullable = false)
    private double salarioBase;

    @Column
    private double horasExtra;

    @Column
    private double deducciones;

    @Column
    private double impuestos;

    @Column
    private double bonificaciones;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoEmpleado estado;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoContrato tipoContrato;

    @OneToOne
    private User user;

}
