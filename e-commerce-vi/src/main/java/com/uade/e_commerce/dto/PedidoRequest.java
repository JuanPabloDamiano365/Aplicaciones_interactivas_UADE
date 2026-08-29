package com.uade.e_commerce.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record PedidoRequest(
        @NotBlank String usuario,
        @NotEmpty List<@Valid PedidoItemRequest> items) {
}
