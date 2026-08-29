package com.uade.e_commerce.dto;

import com.uade.e_commerce.model.OrdenItem;

public record PedidoItemResponse(
        Long productoId,
        String producto,
        int cantidad,
        Double precioUnitario,
        Double subtotal) {

    public static PedidoItemResponse from(OrdenItem item) {
        return new PedidoItemResponse(item.getProducto().getId(), item.getProducto().getNombre(),
                item.getCantidad(), item.getPrecioUnitario(), item.getPrecioUnitario() * item.getCantidad());
    }
}
