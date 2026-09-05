package com.uade.e_commerce.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad JPA que representa el carrito de compras de un usuario.
 *
 * Cada Usuario tiene un único Carrito (relación 1 a 1), que a su vez
 * contiene una lista de ItemCarrito (los productos que fue agregando).
 *
 * Se mapea a la tabla "carrito".
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "carrito")
public class Carrito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Relación uno-a-uno (@OneToOne), lado "dueño" de la relación: acá se
     * crea la columna de clave foránea "usuario_id" en la tabla "carrito".
     */
    @OneToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    private LocalDateTime fechaCreacion;

    /**
     * Relación uno-a-muchos (@OneToMany) hacia los ítems del carrito.
     * - cascade = ALL: al guardar/borrar el carrito, se guardan/borran sus items.
     * - orphanRemoval = true: si un ItemCarrito se saca de esta lista, se
     *   borra directamente de la base de datos (por ejemplo, al eliminar
     *   un producto del carrito).
     */
    @OneToMany(mappedBy = "carrito", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemCarrito> items = new ArrayList<>();
}
