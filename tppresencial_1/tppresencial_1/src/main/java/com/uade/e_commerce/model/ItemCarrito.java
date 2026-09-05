package com.uade.e_commerce.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad JPA que representa una línea/renglón dentro del Carrito: un
 * producto puntual, con un talle y una cantidad elegidos por el usuario.
 *
 * Se mapea a la tabla "itemcarrito".
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "itemcarrito")
public class ItemCarrito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Relación muchos-a-uno hacia Carrito (lado dueño: crea la columna FK
     * "carrito_id"). Se ignora en el JSON de salida para no volver a
     * serializar el carrito completo (Carrito -> items -> Carrito -> ...).
     */
    @ManyToOne
    @JoinColumn(name = "carrito_id")
    @JsonIgnore
    private Carrito carrito;

    // Relación muchos-a-uno hacia Producto: cada item referencia un único producto
    @ManyToOne
    @JoinColumn(name = "producto_id")
    private Producto producto;

    private Integer talle;
    private Integer cantidad;
}
