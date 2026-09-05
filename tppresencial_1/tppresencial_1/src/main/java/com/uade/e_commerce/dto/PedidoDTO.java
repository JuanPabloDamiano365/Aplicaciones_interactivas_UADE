package com.uade.e_commerce.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de respuesta que representa un pedido ya confirmado, con sus items y
 * el total final.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoDTO {

    private Long id;
    private Long usuarioId;
    private LocalDateTime fecha;
    private String estado;
    private Double total;
    private List<ItemPedidoDTO> items;
}
