package com.uade.e_commerce.model;

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
 * Entidad JPA que representa un producto (zapatilla) del catálogo.
 *
 * Se mapea a la tabla "productos" en la base de datos.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "productos")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre; 
    private String genero; 
    private String marca;
    private String descripcion;
    private Double precio;
    private String color;

    /**
     * Cantidad de unidades disponibles en stock. Se descuenta
     * automáticamente cuando se confirma un Pedido (ver PedidoService) y
     * se valida antes de agregar el producto a un Carrito.
     */
    private Integer stock;

    private String imagenUrl; 

    /**
     * Relación muchos-a-uno (@ManyToOne): muchos Productos pueden
     * pertenecer a una misma Categoria. Esta es la parte "dueña" de la
     * relación: es la que efectivamente crea la columna de clave foránea
     * (Foreign Key) en la tabla "productos".
     *
     * @JoinColumn define el nombre de esa columna FK: "categoria_id".
     */
    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;
}
