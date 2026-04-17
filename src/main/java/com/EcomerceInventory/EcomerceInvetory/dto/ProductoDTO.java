package com.EcomerceInventory.EcomerceInvetory.dto;

import com.EcomerceInventory.EcomerceInvetory.model.Categoria;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ProductoDTO {
    @NotBlank(message = "El nombre del producto no puede estar vacio")
    @NotNull(message = "Debe haber un nombre del producto")
    private String nombre;

    @NotBlank(message = "La descripcion del producto no puede estar vacia")
    @NotBlank(message = "Debe haber una descripcion del producto")
    private String descripcin;

    @NotNull(message = "Debe haber un precio para el producto")
    private Double precio;

    @NotNull(message = "Debe haber un stock para el producto")
    private Integer stock;

    private String categoriaNombre;

    @NotNull(message = "Debe haber una categoria para el producto")
    private Long categoriaId;

    private Boolean activo;
}
