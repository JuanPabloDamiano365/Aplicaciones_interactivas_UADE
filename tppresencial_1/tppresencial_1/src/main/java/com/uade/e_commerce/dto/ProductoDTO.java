package com.uade.e_commerce.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de Producto, usado en el body de las peticiones y en las respuestas
 * del ProductoController.
 *
 * En vez de exponer la entidad Producto (que trae el objeto Categoria
 * completo dentro), se expone solamente "categoriaId" (para crear/editar
 * el producto indicando a qué categoría pertenece) y "categoriaNombre"
 * (solo de lectura, para no tener que hacer una consulta aparte al mostrar
 * el nombre de la categoría de cada producto).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductoDTO {

    private Long id;

    @NotBlank(message = "El nombre del producto es obligatorio")
    private String nombre;

    private String genero;
    private String marca;
    private String descripcion;

    @NotNull(message = "El precio es obligatorio")
    @Positive(message = "El precio debe ser mayor a 0")
    private Double precio;

    private String color;

    @NotNull(message = "El stock es obligatorio")
    @PositiveOrZero(message = "El stock no puede ser negativo")
    private Integer stock;

    private String imagenUrl;

    @NotNull(message = "Debe indicarse la categoría del producto")
    private Long categoriaId; // Se usa al crear/editar: a qué categoría pertenece

    private String categoriaNombre; // Solo de lectura: nombre de la categoría (comodidad para el front-end)
}
