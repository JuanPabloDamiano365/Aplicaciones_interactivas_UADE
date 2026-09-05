package com.uade.e_commerce.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.uade.e_commerce.dto.CategoriaDTO;
import com.uade.e_commerce.exception.BusinessException;
import com.uade.e_commerce.exception.ResourceNotFoundException;
import com.uade.e_commerce.model.Categoria;
import com.uade.e_commerce.repository.CategoriaRepository;

/**
 * Capa de Lógica de Negocio (Service) de Categoria.
 *
 * @Service marca la clase como un "bean" de servicio (Spring la detecta e
 * inyecta automáticamente donde se necesite, por ejemplo en el
 * Controller).
 *
 * @Transactional hace que cada método público se ejecute dentro de una
 * transacción de base de datos: si algo falla a mitad de camino, se
 * revierten (rollback) todos los cambios hechos hasta ese punto, evitando
 * dejar la base de datos en un estado inconsistente.
 *
 * Acá también viven los métodos "toDTO"/"toEntity" que convierten entre la
 * entidad JPA (Categoria) y el DTO (CategoriaDTO) expuesto por la API.
 */
@Service
@Transactional
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    // Inyección de dependencias por constructor (la forma recomendada por Spring)
    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    // Convierte una entidad Categoria en su DTO correspondiente
    private CategoriaDTO toDTO(Categoria categoria) {
        return new CategoriaDTO(categoria.getId(), categoria.getNombre());
    }

    // Devuelve todas las categorías cargadas en la base de datos
    public List<CategoriaDTO> getAllCategorias() {
        return categoriaRepository.findAll().stream()
                .map(this::toDTO)
                .toList();
    }

    // Busca una categoría por id; si no existe, lanza ResourceNotFoundException (-> 404)
    public CategoriaDTO getCategoriaById(Long id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró la categoría con id " + id));
        return toDTO(categoria);
    }

    // Crea una nueva categoría, validando que no exista otra con el mismo nombre
    public CategoriaDTO crearCategoria(CategoriaDTO dto) {
        if (categoriaRepository.existsByNombreIgnoreCase(dto.getNombre())) {
            throw new BusinessException("Ya existe una categoría con el nombre '" + dto.getNombre() + "'");
        }
        Categoria categoria = new Categoria();
        categoria.setNombre(dto.getNombre());
        Categoria guardada = categoriaRepository.save(categoria); 
        return toDTO(guardada);
    }

    // Actualiza el nombre de una categoría existente
    public CategoriaDTO actualizarCategoria(Long id, CategoriaDTO dto) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró la categoría con id " + id));
        categoria.setNombre(dto.getNombre());
        Categoria actualizada = categoriaRepository.save(categoria); 
        return toDTO(actualizada);
    }

    // Elimina una categoría por id
    public void eliminarCategoria(Long id) {
        if (!categoriaRepository.existsById(id)) {
            throw new ResourceNotFoundException("No se encontró la categoría con id " + id);
        }
        categoriaRepository.deleteById(id);
    }
}
