package com.uade.e_commerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uade.e_commerce.model.ItemPedido;

/**
 * Repositorio de ItemPedido. No necesita métodos extra: se accede a los
 * items siempre a través del Pedido al que pertenecen.
 */
@Repository
public interface ItemPedidoRepository extends JpaRepository<ItemPedido, Long> {
}
