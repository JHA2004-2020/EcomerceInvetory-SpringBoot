package com.EcomerceInventory.EcomerceInvetory.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class DetallesFactura {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(nullable = false)
    private Double precioMomento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id")
    private Producto producto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "factura_id")
    private Factura factura;
}
