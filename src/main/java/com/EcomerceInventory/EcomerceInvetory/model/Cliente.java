package com.EcomerceInventory.EcomerceInvetory.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Boolean activo;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false, unique = true)
    private String identificacion;

    @Column(nullable = false)
    private Integer edad;

    @Column(nullable = false, unique = false)
    private String correo;

    @Column(nullable = false)
    private String pasword;

    @OneToMany(mappedBy = "cliente")
    private List<Factura> facturas;

    @OneToOne(mappedBy = "cliente")
    private TarjetaPuntos tarjetaPuntos;
}
