package com.uade.e_commerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.uade.e_commerce.model.Producto;

/**
 * JpaRepository Provides CRUD operations and additional query methods for the Producto
 * save, update, delete, findById, findAll, etc. de la tabla productos
 * ProductoRepository
 */
public interface ProductoRepository extends JpaRepository<Producto, Long> {
	List<Producto> findByActivoTrueAndNombreContainingIgnoreCase(String nombre);
	List<Producto> findByActivoTrueAndNombreContainingIgnoreCaseAndCategoriaIgnoreCase(String nombre, String categoria);
	List<Producto> findByActivoTrueAndNombreContainingIgnoreCaseAndTalle(String nombre, Integer talle);
	List<Producto> findByActivoTrueAndNombreContainingIgnoreCaseAndCategoriaIgnoreCaseAndTalle(String nombre,
			String categoria, Integer talle);
}
