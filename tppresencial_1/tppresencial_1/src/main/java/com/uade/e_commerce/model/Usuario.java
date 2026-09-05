package com.uade.e_commerce.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad JPA que representa a un usuario/cliente de la tienda.
 *
 * Este archivo estaba vacío en el proyecto original; se completó acá
 * porque Producto, Carrito y Pedido ya hacían referencia a "Usuario".
 *
 * Se mapea a la tabla "usuarios".
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    // @Column(unique = true) evita que se registren dos usuarios con el mismo email
    @Column(unique = true)
    private String email;

    private String password;
    private String direccion;
    // TODO el cliente puede tener varios registros.
    private String telefono;

    /**
     * Relación uno-a-uno (@OneToOne): cada Usuario tiene un único Carrito
     * asociado (se crea automáticamente al registrar el usuario, ver
     * UsuarioService). "mappedBy" indica que la relación está mapeada del
     * lado de Carrito (atributo "usuario" en esa clase).
     */
    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore // Se evita serializar el carrito completo junto con el usuario (se consulta aparte)
    private Carrito carrito;

    /**
     * Relación uno-a-muchos (@OneToMany): un Usuario puede tener muchos
     * Pedidos realizados a lo largo del tiempo.
     */
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    @JsonIgnore // Se evita serializar todos los pedidos junto con el usuario (se consultan por endpoint aparte)
    private List<Pedido> pedidos = new ArrayList<>();
}
