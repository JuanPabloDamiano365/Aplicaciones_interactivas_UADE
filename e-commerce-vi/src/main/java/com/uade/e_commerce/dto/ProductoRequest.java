package com.uade.e_commerce.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProductoRequest(
        @NotBlank String nombre,
        String marca,
        String sexo,
        int talle,
        String descripcion,
        String imagenUrl,
        @NotNull @Min(0) Double precio,
        @Min(0) int stock,
        String categoria,
        boolean activo) {
}
