package com.uade.e_commerce.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.uade.e_commerce.model.Producto;
import com.uade.e_commerce.dto.ProductoRequest;
import com.uade.e_commerce.repository.ProductoRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public List<Producto> getAllProductos() {
        // select * from productos
        return productoRepository.findAll();
    }

    public Optional<Producto> getProductoById(Long id) {
        return productoRepository.findById(id);
    }

    public Producto guardarProducto(ProductoRequest request) {
        return productoRepository.save(toEntity(request));
    }

    public Optional<Producto> actualizarProducto(Long id, ProductoRequest datos) {
        return productoRepository.findById(id).map(producto -> {
            producto.setNombre(datos.nombre());
            producto.setMarca(datos.marca());
            producto.setSexo(datos.sexo());
            producto.setTalle(datos.talle());
            producto.setDescripcion(datos.descripcion());
            producto.setImagenUrl(datos.imagenUrl());
            producto.setPrecio(datos.precio());
            producto.setStock(datos.stock());
            producto.setCategoria(datos.categoria());
            producto.setActivo(datos.activo());
            return productoRepository.save(producto);
        });
    }

    private Producto toEntity(ProductoRequest request) {
        return new Producto(null, request.nombre(), request.marca(), request.sexo(), request.talle(),
                request.descripcion(), request.imagenUrl(), request.precio(), request.stock(), request.categoria(),
                request.activo());
    }

    public List<Producto> buscarProductos(String texto, String categoria, Integer talle) {
        String busqueda = texto == null ? "" : texto.trim();
        if (categoria != null && talle != null) {
            return productoRepository.findByActivoTrueAndNombreContainingIgnoreCaseAndCategoriaIgnoreCaseAndTalle(
                    busqueda, categoria, talle);
        }
        if (categoria != null) {
            return productoRepository.findByActivoTrueAndNombreContainingIgnoreCaseAndCategoriaIgnoreCase(busqueda,
                    categoria);
        }
        if (talle != null) {
            return productoRepository.findByActivoTrueAndNombreContainingIgnoreCaseAndTalle(busqueda, talle);
        }
        return productoRepository.findByActivoTrueAndNombreContainingIgnoreCase(busqueda);
    }

    public boolean eliminarProducto(Long id) {
        if (!productoRepository.existsById(id)) {
            return false;
        }
        productoRepository.deleteById(id);
        return true;
    }
}
