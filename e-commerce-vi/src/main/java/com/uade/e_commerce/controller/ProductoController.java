package com.uade.e_commerce.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uade.e_commerce.dto.ProductoRequest;
import com.uade.e_commerce.dto.ProductoResponse;
import com.uade.e_commerce.service.ProductoService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import com.uade.e_commerce.exceptions.ResourceNotFoundException;



// http://localhost:8080/api/productos
@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }


    // get http://localhost:8080/api/productos
    @GetMapping()
    public List<ProductoResponse> getAllProductos() {
        return productoService.getAllProductos().stream().map(ProductoResponse::from).toList();
    }

    //get http://localhost:8080/api/productos/1 
    @GetMapping("/{id}")
    public ResponseEntity<ProductoResponse> getProductoById(@PathVariable Long id) {
        return productoService.getProductoById(id)
                .map(ProductoResponse::from)
                .map(ResponseEntity::ok)
            .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado: " + id));
    }

    @GetMapping("/buscar")
    public List<ProductoResponse> buscarProductos(@RequestParam(required = false) String texto,
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) Integer talle) {
        return productoService.buscarProductos(texto, categoria, talle).stream().map(ProductoResponse::from).toList();
    }

    @PostMapping
    public ResponseEntity<ProductoResponse> crearProducto(@Valid @RequestBody ProductoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ProductoResponse.from(productoService.guardarProducto(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductoResponse> actualizarProducto(@PathVariable Long id, @Valid @RequestBody ProductoRequest request) {
        return productoService.actualizarProducto(id, request)
                .map(ProductoResponse::from)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado: " + id));
    }

    //borra el producto 1 delete http://localhost:8080/api/productos/1 
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProducto(@PathVariable Long id) {
        if (!productoService.eliminarProducto(id)) {
            throw new ResourceNotFoundException("Producto no encontrado: " + id);
        }
        return ResponseEntity.noContent().build();
    }
}

