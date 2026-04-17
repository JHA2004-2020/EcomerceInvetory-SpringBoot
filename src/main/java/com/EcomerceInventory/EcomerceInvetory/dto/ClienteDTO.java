package com.EcomerceInventory.EcomerceInvetory.dto;

import com.EcomerceInventory.EcomerceInvetory.model.TarjetaPuntos;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ClienteDTO {
    @NotBlank(message = "El nombre no puede estar vacio")
    @NotNull(message = "Debe haber un nombre de cliente")
    private String nombre;

    @NotBlank(message = "La identificiacion no debe estar vacia")
    @NotNull(message = "Debe haber una identificiacion para el cliente")
    private String identificacion;

    @NotNull(message = "Debe haber una edad")
    private Integer edad;

    @NotBlank(message = "El correo no puede estar vacio")
    @NotNull(message = "Debe haber un correo")
    private String correo;

    @NotNull(message = "La contraseña no puede estar vacia")
    @NotNull(message = "Debe haber una contraseña")
    private String pasword;

    private Boolean activo;

    private Boolean tarjetaActivo;
    private String tarjetaCodigo;
    private Integer tarjetaPuntos;
}
