package com.uade.e_commerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de solo lectura para mostrar un ítem dentro de un CarritoDTO. Incluye
 * datos "aplanados" del producto (nombre, precio, subtotal) para que el
 * front-end no tenga que hacer una consulta extra por cada item.
 */
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
    private Double subtotal; // precioUnitario * cantidad, calculado en el Service
}
