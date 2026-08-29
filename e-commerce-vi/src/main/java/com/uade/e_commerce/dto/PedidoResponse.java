package com.uade.e_commerce.dto;

import com.uade.e_commerce.model.Ordenes;
import java.time.LocalDateTime;
import java.util.List;

public record PedidoResponse(
        Long id,
        String usuario,
        LocalDateTime fecha,
        String estado,
        Double total,
        List<PedidoItemResponse> items) {

    public static PedidoResponse from(Ordenes pedido) {
        return new PedidoResponse(pedido.getId(), pedido.getUsuario(), pedido.getFecha(), pedido.getEstado(),
                pedido.getTotal(), pedido.getItems().stream().map(PedidoItemResponse::from).toList());
    }
}
