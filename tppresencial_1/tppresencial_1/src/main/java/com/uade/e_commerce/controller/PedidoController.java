package com.uade.e_commerce.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uade.e_commerce.dto.ActualizarEstadoDTO;
import com.uade.e_commerce.dto.PedidoDTO;
import com.uade.e_commerce.service.PedidoService;

import jakarta.validation.Valid;

/**
 * Capa Controller de Pedido.
 *
 * Incluye el endpoint de checkout (POST /api/usuarios/{id}/pedidos), que
 * dispara la lógica de negocio de confirmar la compra a partir del
 * carrito actual del usuario.
 */
@RestController
@RequestMapping("/api")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @GetMapping("/pedidos")
    public ResponseEntity<List<PedidoDTO>> getAllPedidos() {
        return ResponseEntity.ok(pedidoService.getAllPedidos());
    }

    @GetMapping("/pedidos/{id}")
    public ResponseEntity<PedidoDTO> getPedidoById(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.getPedidoById(id));
    }

    @GetMapping("/usuarios/{usuarioId}/pedidos")
    public ResponseEntity<List<PedidoDTO>> getPedidosByUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(pedidoService.getPedidosByUsuario(usuarioId));
    }

    @PostMapping("/usuarios/{usuarioId}/pedidos")
    public ResponseEntity<PedidoDTO> crearPedido(@PathVariable Long usuarioId) {
        PedidoDTO creado = pedidoService.crearPedidoDesdeCarrito(usuarioId);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @PutMapping("/pedidos/{id}/estado")
    public ResponseEntity<PedidoDTO> actualizarEstado(@PathVariable Long id, @Valid @RequestBody ActualizarEstadoDTO dto) {
        return ResponseEntity.ok(pedidoService.actualizarEstado(id, dto.getEstado()));
    }

    @PutMapping("/pedidos/{id}/cancelar")
    public ResponseEntity<PedidoDTO> cancelarPedido(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.cancelarPedido(id));
    }
}
