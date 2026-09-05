package com.uade.e_commerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de solo lectura para mostrar un ítem dentro de un PedidoDTO.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemPedidoDTO {

    private Long id;
    private Long productoId;
    private String productoNombre;
    private Integer talle;
    private Integer cantidad;
    private Double precioUnitario;
    private Double subtotal; // precioUnitario * cantidad
}
