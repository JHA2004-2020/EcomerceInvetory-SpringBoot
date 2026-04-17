package com.EcomerceInventory.EcomerceInvetory.dto;

import com.EcomerceInventory.EcomerceInvetory.model.Cliente;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class TarjetaPuntosDTO {
    private Boolean activo;

    private String codigo;

    private Integer puntos;

    private String nombreCliente;
}
