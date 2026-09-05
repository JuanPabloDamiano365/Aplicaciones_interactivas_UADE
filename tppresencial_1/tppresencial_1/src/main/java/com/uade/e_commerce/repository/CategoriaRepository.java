package com.uade.e_commerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uade.e_commerce.model.Categoria;

/**
 * Repositorio de Categoria.
 *
 * Al extender JpaRepository<Categoria, Long> se obtienen "gratis" (sin
 * escribir una sola línea de SQL) métodos como save(), findById(),
 * findAll(), deleteById(), existsById(), count(), etc. Spring Data JPA
 * genera la implementación real en tiempo de ejecución.
 *
 * @Repository marca la clase como un "bean" de la capa de acceso a datos
 * (no es estrictamente obligatoria sobre interfaces que extienden
 * JpaRepository, ya que Spring Data las detecta igual, pero se agrega
 * para dejar explícita la capa a la que pertenece, tal como pide la
 * consigna).
 */
@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    // Método de consulta derivado: Spring Data JPA arma automáticamente el
    // "SELECT * FROM categoria WHERE nombre = ?" a partir del nombre del método.
    boolean existsByNombreIgnoreCase(String nombre);
}
