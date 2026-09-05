package com.uade.e_commerce.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de request muy chico, usado solo para el endpoint
 * PUT /api/pedidos/{id}/estado, que cambia el estado de un pedido
 * (por ejemplo, de "PENDIENTE" a "ENVIADO").
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActualizarEstadoDTO {

    @NotBlank(message = "Debe indicarse el nuevo estado")
    private String estado;
}
