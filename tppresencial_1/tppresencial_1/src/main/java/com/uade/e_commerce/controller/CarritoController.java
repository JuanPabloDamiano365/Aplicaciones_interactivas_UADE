package com.uade.e_commerce.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uade.e_commerce.dto.AgregarItemCarritoDTO;
import com.uade.e_commerce.dto.CarritoDTO;
import com.uade.e_commerce.service.CarritoService;

import jakarta.validation.Valid;

/**
 * Capa Controller de Carrito.
 *
 * El carrito no se identifica por su propio id en la URL sino por el id
 * del Usuario dueño (más natural para el consumidor de la API: "el
 * carrito del usuario X"), ya que la relación Usuario-Carrito es 1 a 1.
 */
@RestController
@RequestMapping("/api/usuarios/{usuarioId}/carrito")
public class CarritoController {

    private final CarritoService carritoService;

    public CarritoController(CarritoService carritoService) {
        this.carritoService = carritoService;
    }

    @GetMapping
    public ResponseEntity<CarritoDTO> getCarrito(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(carritoService.getCarritoByUsuario(usuarioId));
    }

    // Devuelve el carrito completo ya actualizado, para que el front-end no tenga que pedirlo aparte.
    @PostMapping("/items")
    public ResponseEntity<CarritoDTO> agregarItem(@PathVariable Long usuarioId,
                                                   @Valid @RequestBody AgregarItemCarritoDTO request) {
        return ResponseEntity.ok(carritoService.agregarItem(usuarioId, request));
    }

    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<CarritoDTO> eliminarItem(@PathVariable Long usuarioId, @PathVariable Long itemId) {
        return ResponseEntity.ok(carritoService.eliminarItem(usuarioId, itemId));
    }

    @DeleteMapping
    public ResponseEntity<Void> vaciarCarrito(@PathVariable Long usuarioId) {
        carritoService.vaciarCarrito(usuarioId);
        return ResponseEntity.noContent().build();
    }
}
