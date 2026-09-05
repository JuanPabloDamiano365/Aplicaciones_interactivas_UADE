package com.uade.e_commerce.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad JPA que representa una categoría de productos (ej: "Running",
 * "Urbanas", "Botines", "Basketball").
 *
 * Cada clase anotada con @Entity se mapea a una tabla de la base de datos
 * (acá, la tabla "categoria" gracias a @Table). Cada atributo se mapea a
 * una columna de esa tabla.
 */
@Data 
@NoArgsConstructor 
@AllArgsConstructor 
@Entity 
@Table(name = "categoria") // Nombre explícito de la tabla en la base de datos
public class Categoria {

    @Id // Indica que este atributo es la clave primaria (Primary Key) de la tabla
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Long id;

    private String nombre;

    /**
     * Relación uno-a-muchos (@OneToMany): una Categoria puede tener muchos
     * Productos asociados. "mappedBy" indica que la relación ya está
     * mapeada del lado de Producto (en el atributo "categoria" de esa
     * clase), por lo que acá NO se genera una columna nueva, solo se lee
     * la relación inversa.
     *
     * - cascade = CascadeType.ALL: si se borra/persiste una Categoria, la
     *   operación se propaga a sus Productos asociados.
     * - @JsonIgnore: evita que, al serializar una Categoria a JSON, se
     *   intente serializar también la lista completa de productos (lo que
     *   generaría respuestas enormes o loops infinitos Categoria->Producto->
     *   Categoria->...). Para mostrar productos de una categoría se usa el
     *   endpoint específico GET /api/productos/categoria/{id}.
     */
    @OneToMany(mappedBy = "categoria", cascade = CascadeType.ALL, orphanRemoval = false)
    @JsonIgnore
    private List<Producto> productos = new ArrayList<>();
}
