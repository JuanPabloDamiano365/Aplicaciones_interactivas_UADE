package com.uade.e_commerce.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de respuesta que representa el carrito completo de un usuario: sus
 * items (ItemCarritoDTO) y el total acumulado.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarritoDTO {

    private Long id;
    private Long usuarioId;
    private LocalDateTime fechaCreacion;
    private List<ItemCarritoDTO> items;
    private Double total; // Suma de los subtotales de todos los items
}
