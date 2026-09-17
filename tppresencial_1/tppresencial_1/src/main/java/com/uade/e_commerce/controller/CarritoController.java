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

@RestController
@RequestMapping("/api/usuarios/{usuarioId}/carrito")
public class CarritoController {

    private final CarritoService carritoService;

    public CarritoController(CarritoService carritoService) {
        this.carritoService = carritoService;
    }

    @GetMapping
    public ResponseEntity<CarritoDTO> getCarrito(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(carritoService.buscarCarritoPorUsuario(usuarioId));
    }
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
