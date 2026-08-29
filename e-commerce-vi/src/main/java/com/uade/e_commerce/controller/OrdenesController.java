package com.uade.e_commerce.controller;

import com.uade.e_commerce.dto.PedidoRequest;
import com.uade.e_commerce.dto.PedidoResponse;
import com.uade.e_commerce.exceptions.ResourceNotFoundException;
import com.uade.e_commerce.service.OrdenesService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ordenes")
public class OrdenesController {

    private final OrdenesService ordenesService;

    public OrdenesController(OrdenesService ordenesService) {
        this.ordenesService = ordenesService;
    }

    @GetMapping
    public ResponseEntity<List<PedidoResponse>> listarOrdenes() {
        return ResponseEntity.ok(ordenesService.listarOrdenes().stream().map(PedidoResponse::from).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponse> buscarOrden(@PathVariable Long id) {
        return ordenesService.buscarPorId(id)
                .map(PedidoResponse::from)
                .map(ResponseEntity::ok)
            .orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado: " + id));
    }

    @PostMapping
    public ResponseEntity<PedidoResponse> crearOrden(@Valid @RequestBody PedidoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(PedidoResponse.from(ordenesService.crearOrden(request)));
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<PedidoResponse> actualizarEstado(@PathVariable Long id, @RequestParam String estado) {
        return ordenesService.actualizarEstado(id, estado)
                .map(PedidoResponse::from)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado: " + id));
    }
}
