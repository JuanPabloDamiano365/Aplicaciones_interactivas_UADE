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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad JPA que representa un pedido/orden de compra ya confirmado
 * (a diferencia del Carrito, que es "provisorio" mientras el usuario
 * sigue comprando).
 *
 * Se mapea a la tabla "pedido".
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pedido")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relación muchos-a-uno: muchos pedidos pueden pertenecer a un mismo usuario
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    private LocalDateTime fecha;

    // Estados posibles: PENDIENTE, ENVIADO, ENTREGADO, CANCELADO
    private String estado;

    // Monto total del pedido, calculado como la suma de (precioUnitario * cantidad) de cada item
    private Double total;

    /**
     * Relación uno-a-muchos hacia los items que componen el pedido.
     * cascade = ALL: al guardar/borrar el pedido se guardan/borran sus items.
     */
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemPedido> items = new ArrayList<>();
}
