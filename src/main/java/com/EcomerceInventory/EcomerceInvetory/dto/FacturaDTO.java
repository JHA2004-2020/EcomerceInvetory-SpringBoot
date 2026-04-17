package com.EcomerceInventory.EcomerceInvetory.dto;

import com.EcomerceInventory.EcomerceInvetory.model.Cliente;
import com.EcomerceInventory.EcomerceInvetory.model.DetallesFactura;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class FacturaDTO {
    private LocalDateTime fecha;

    private Double total;

    private Long cliente;

    private List<DetallesFacturaDTO> detallesFacturas;

}
