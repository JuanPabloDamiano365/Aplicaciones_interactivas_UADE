package com.uade.e_commerce.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uade.e_commerce.model.ItemCarrito;

/**
 * Repositorio de ItemCarrito.
 */
@Repository
public interface ItemCarritoRepository extends JpaRepository<ItemCarrito, Long> {

    // Se usa para detectar si el producto+talle ya está en el carrito y así
    // sumar la cantidad en vez de crear un ítem duplicado.
    Optional<ItemCarrito> findByCarritoIdAndProductoIdAndTalle(Long carritoId, Long productoId, Integer talle);
}
