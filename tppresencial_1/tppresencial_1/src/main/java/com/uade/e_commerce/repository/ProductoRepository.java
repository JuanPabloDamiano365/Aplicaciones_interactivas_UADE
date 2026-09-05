package com.uade.e_commerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uade.e_commerce.model.Producto;

/**
 * Repositorio de Producto: CRUD básico (save, findById, findAll,
 * deleteById...) heredado de JpaRepository, más un método de consulta
 * derivado para filtrar productos por categoría.
 */
@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    List<Producto> findByCategoriaId(Long categoriaId);

    List<Producto> findByGeneroIgnoreCase(String genero);
}
