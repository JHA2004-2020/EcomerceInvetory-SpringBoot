package com.EcomerceInventory.EcomerceInvetory.dto;

import com.EcomerceInventory.EcomerceInvetory.model.Factura;
import com.EcomerceInventory.EcomerceInvetory.model.Producto;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class DetallesFacturaDTO {
    @NotNull(message = "Debe haber una cantidad para el detalle del la compra del producto")
    private Integer cantidad;

    @NotNull(message = "Debe haber un id que identifique el producto")
    private Long productoId;

    private Double precioMomento;

    private String nombreProducto;

}
