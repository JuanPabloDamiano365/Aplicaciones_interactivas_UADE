package com.uade.e_commerce.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.uade.e_commerce.dto.ProductoDTO;
import com.uade.e_commerce.exception.ResourceNotFoundException;
import com.uade.e_commerce.model.Categoria;
import com.uade.e_commerce.model.Producto;
import com.uade.e_commerce.repository.CategoriaRepository;
import com.uade.e_commerce.repository.ProductoRepository;

/**
 * Capa de Lógica de Negocio (Service) de Producto.
 */
@Service
@Transactional
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository; // Se necesita para validar/asociar la categoría del producto

    public ProductoService(ProductoRepository productoRepository, CategoriaRepository categoriaRepository) {
        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    // Convierte la entidad Producto (con su relación a Categoria) en un ProductoDTO "aplanado"
    private ProductoDTO toDTO(Producto producto) {
        ProductoDTO dto = new ProductoDTO();
        dto.setId(producto.getId());
        dto.setNombre(producto.getNombre());
        dto.setGenero(producto.getGenero());
        dto.setMarca(producto.getMarca());
        dto.setDescripcion(producto.getDescripcion());
        dto.setPrecio(producto.getPrecio());
        dto.setColor(producto.getColor());
        dto.setStock(producto.getStock());
        dto.setImagenUrl(producto.getImagenUrl());
        if (producto.getCategoria() != null) {
            dto.setCategoriaId(producto.getCategoria().getId());
            dto.setCategoriaNombre(producto.getCategoria().getNombre());
        }
        return dto;
    }

    public List<ProductoDTO> getAllProductos() {
        return productoRepository.findAll().stream()
                .map(this::toDTO)
                .toList();
    }

    // Busca un producto por id; si no existe, 404
    public ProductoDTO getProductoById(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el producto con id " + id));
        return toDTO(producto);
    }

    // Lista los productos que pertenecen a una categoría puntual
    public List<ProductoDTO> getProductosByCategoria(Long categoriaId) {
        if (!categoriaRepository.existsById(categoriaId)) {
            throw new ResourceNotFoundException("No se encontró la categoría con id " + categoriaId);
        }
        return productoRepository.findByCategoriaId(categoriaId).stream()
                .map(this::toDTO)
                .toList();
    }

    // Lista los productos filtrados por género ("Hombre", "Mujer", "Unisex")
    public List<ProductoDTO> getProductosByGenero(String genero) {
        return productoRepository.findByGeneroIgnoreCase(genero).stream()
                .map(this::toDTO)
                .toList();
    }

    // Crea un producto nuevo, validando que la categoría indicada exista
    public ProductoDTO crearProducto(ProductoDTO dto) {
        Categoria categoria = categoriaRepository.findById(dto.getCategoriaId())
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró la categoría con id " + dto.getCategoriaId()));

        Producto producto = new Producto();
        producto.setNombre(dto.getNombre());
        producto.setGenero(dto.getGenero());
        producto.setMarca(dto.getMarca());
        producto.setDescripcion(dto.getDescripcion());
        producto.setPrecio(dto.getPrecio());
        producto.setColor(dto.getColor());
        producto.setStock(dto.getStock());
        producto.setImagenUrl(dto.getImagenUrl());
        producto.setCategoria(categoria);

        Producto guardado = productoRepository.save(producto); 
        return toDTO(guardado);
    }

    // Actualiza todos los datos editables de un producto existente
    public ProductoDTO actualizarProducto(Long id, ProductoDTO dto) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el producto con id " + id));

        Categoria categoria = categoriaRepository.findById(dto.getCategoriaId())
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró la categoría con id " + dto.getCategoriaId()));

        producto.setNombre(dto.getNombre());
        producto.setGenero(dto.getGenero());
        producto.setMarca(dto.getMarca());
        producto.setDescripcion(dto.getDescripcion());
        producto.setPrecio(dto.getPrecio());
        producto.setColor(dto.getColor());
        producto.setStock(dto.getStock());
        producto.setImagenUrl(dto.getImagenUrl());
        producto.setCategoria(categoria);

        Producto actualizado = productoRepository.save(producto); 
        return toDTO(actualizado);
    }

    // Elimina un producto por id
    public void eliminarProducto(Long id) {
        if (!productoRepository.existsById(id)) {
            throw new ResourceNotFoundException("No se encontró el producto con id " + id);
        }
        productoRepository.deleteById(id);
    }
}
