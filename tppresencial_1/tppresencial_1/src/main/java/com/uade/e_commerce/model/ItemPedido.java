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
 * Entidad JPA que representa una línea de un Pedido: un producto puntual,
 * con talle, cantidad y el precio unitario que tenía el producto EN EL
 * MOMENTO de la compra (se copia el precio para que, si el precio del
 * producto cambia después, no se altere el historial de pedidos ya
 * realizados).
 *
 * Se mapea a la tabla "itempedido".
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "itempedido")
public class ItemPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relación muchos-a-uno hacia Pedido (lado dueño: columna FK "pedido_id")
    @ManyToOne
    @JoinColumn(name = "pedido_id")
    @JsonIgnore
    private Pedido pedido;

    // Relación muchos-a-uno hacia Producto
    @ManyToOne
    @JoinColumn(name = "producto_id")
    private Producto producto;

    private Integer talle;
    private Integer cantidad;

    private Double precioUnitario;
}
