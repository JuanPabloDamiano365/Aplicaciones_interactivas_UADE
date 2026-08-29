package com.uade.e_commerce.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record PedidoItemRequest(
        @NotNull Long productoId,
        @Min(1) int cantidad) {
}
