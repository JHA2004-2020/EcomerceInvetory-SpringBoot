package com.EcomerceInventory.EcomerceInvetory.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class TarjetaPuntos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Boolean activo;

    @Column(nullable = false, unique = true)
    private String codigo;

    @Column(nullable = false)
    private Integer puntos;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

}
