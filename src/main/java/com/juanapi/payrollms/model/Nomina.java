package com.juanapi.payrollms.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;


@Getter
@Setter
@Entity
public class Nomina {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate fechaGeneracion;

    @OneToMany(mappedBy = "nomina", cascade = CascadeType.ALL)
    private List<Recibo> recibosPago;

}
