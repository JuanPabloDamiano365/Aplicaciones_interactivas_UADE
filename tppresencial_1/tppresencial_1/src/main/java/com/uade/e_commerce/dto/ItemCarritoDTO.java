package com.uade.e_commerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemCarritoDTO {

    private Long id;
    private Long productoId;
    private String productoNombre;
    private Double precioUnitario;
    private Integer talle;
    private Integer cantidad;
    private Double subtotal;
}
