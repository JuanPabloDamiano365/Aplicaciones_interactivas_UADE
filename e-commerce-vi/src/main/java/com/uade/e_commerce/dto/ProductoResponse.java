package com.uade.e_commerce.dto;

import com.uade.e_commerce.model.Producto;

public record ProductoResponse(
        Long id,
        String nombre,
        String marca,
        String sexo,
        int talle,
        String descripcion,
        String imagenUrl,
        Double precio,
        int stock,
        String categoria,
        boolean activo) {

    public static ProductoResponse from(Producto producto) {
        return new ProductoResponse(producto.getId(), producto.getNombre(), producto.getMarca(), producto.getSexo(),
                producto.getTalle(), producto.getDescripcion(), producto.getImagenUrl(), producto.getPrecio(),
                producto.getStock(), producto.getCategoria(), producto.isActivo());
    }
}
