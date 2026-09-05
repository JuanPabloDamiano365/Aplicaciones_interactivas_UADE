package com.uade.e_commerce.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO (Data Transfer Object) de Categoria.
 *
 * ¿Para qué sirve un DTO? Para que el Controller y quien consume la API
 * (front-end, Postman, etc.) NUNCA reciban ni envíen directamente la
 * entidad JPA (Categoria). Esto desacopla el "modelo de base de datos" del
 * "modelo de la API": se puede cambiar la entidad (agregar una relación,
 * una columna interna, etc.) sin romper el contrato de la API, y se evitan
 * problemas de serialización infinita entre entidades relacionadas
 * (Categoria -> Producto -> Categoria -> ...).
 *
 * Este mismo DTO se usa tanto para las peticiones (POST/PUT, donde "id"
 * llega en null o se ignora) como para las respuestas (GET, donde "id" sí
 * viene completo).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaDTO {

    private Long id;

    @NotBlank(message = "El nombre de la categoría es obligatorio")
    private String nombre;
}
