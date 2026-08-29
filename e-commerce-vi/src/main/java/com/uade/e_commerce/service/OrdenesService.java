package com.uade.e_commerce.service;

import com.uade.e_commerce.model.OrdenItem;
import com.uade.e_commerce.model.Ordenes;
import com.uade.e_commerce.model.Producto;
import com.uade.e_commerce.dto.PedidoRequest;
import com.uade.e_commerce.exceptions.ArgumentInvalidException;
import com.uade.e_commerce.exceptions.ResourceNotFoundException;
import com.uade.e_commerce.repository.OrdenesRepository;
import com.uade.e_commerce.repository.ProductoRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;
import java.util.Set;
import java.util.Locale;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class OrdenesService {

    private static final Set<String> ESTADOS_VALIDOS = Set.of("PENDIENTE", "PAGADA", "ENVIADA", "ENTREGADA", "CANCELADA");

    private final OrdenesRepository ordenesRepository;
    private final ProductoRepository productoRepository;

    public OrdenesService(OrdenesRepository ordenesRepository, ProductoRepository productoRepository) {
        this.ordenesRepository = ordenesRepository;
        this.productoRepository = productoRepository;
    }

    public List<Ordenes> listarOrdenes() {
        return ordenesRepository.findAll();
    }

    public Optional<Ordenes> buscarPorId(Long id) {
        return ordenesRepository.findById(id);
    }

    public Ordenes crearOrden(PedidoRequest request) {
        Ordenes orden = new Ordenes(null, request.usuario(), null, null, null, new ArrayList<>());
        request.items().forEach(item -> {
            Producto referencia = new Producto();
            referencia.setId(item.productoId());
            orden.agregarItem(new OrdenItem(null, referencia, null, item.cantidad(), null));
        });
        return procesarOrden(orden);
    }

    private Ordenes procesarOrden(Ordenes orden) {
        orden.setId(null);
        orden.setFecha(LocalDateTime.now());
        orden.setEstado("PENDIENTE");
        if (orden.getItems() == null || orden.getItems().isEmpty()) {
            throw new ArgumentInvalidException("La orden debe incluir al menos un producto");
        }
        orden.setItems(new ArrayList<>(orden.getItems()));
        double total = 0;
        for (OrdenItem item : orden.getItems()) {
            if (item.getProducto() == null || item.getProducto().getId() == null) {
                throw new ArgumentInvalidException("Cada ítem debe indicar un producto válido");
            }
            Producto producto = productoRepository.findById(item.getProducto().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Producto inexistente: " + item.getProducto().getId()));
            if (!producto.isActivo() || item.getCantidad() <= 0 || producto.getStock() < item.getCantidad()) {
                throw new ArgumentInvalidException("Stock insuficiente o producto inactivo: " + producto.getId());
            }
            item.setProducto(producto);
            item.setPrecioUnitario(producto.getPrecio());
            producto.setStock(producto.getStock() - item.getCantidad());
            total += producto.getPrecio() * item.getCantidad();
            item.setOrden(orden);
        }
        orden.setTotal(total);
        return ordenesRepository.save(orden);
    }

    public Optional<Ordenes> actualizarEstado(Long id, String estado) {
        if (estado == null || !ESTADOS_VALIDOS.contains(estado.trim().toUpperCase(Locale.ROOT))) {
            throw new ArgumentInvalidException("Estado inválido. Valores permitidos: " + ESTADOS_VALIDOS);
        }
        return ordenesRepository.findById(id).map(orden -> {
            orden.setEstado(estado.trim().toUpperCase(Locale.ROOT));
            return ordenesRepository.save(orden);
        });
    }
}
