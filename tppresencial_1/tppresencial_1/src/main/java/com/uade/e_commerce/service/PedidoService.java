package com.uade.e_commerce.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.uade.e_commerce.dto.ItemPedidoDTO;
import com.uade.e_commerce.dto.PedidoDTO;
import com.uade.e_commerce.exception.BusinessException;
import com.uade.e_commerce.exception.ResourceNotFoundException;
import com.uade.e_commerce.model.Carrito;
import com.uade.e_commerce.model.ItemCarrito;
import com.uade.e_commerce.model.ItemPedido;
import com.uade.e_commerce.model.Pedido;
import com.uade.e_commerce.model.Producto;
import com.uade.e_commerce.repository.CarritoRepository;
import com.uade.e_commerce.repository.PedidoRepository;
import com.uade.e_commerce.repository.ProductoRepository;

/**
 * Capa de Lógica de Negocio (Service) de Pedido.
 *
 * Contiene la operación más importante del dominio: "confirmar compra"
 * (crearPedidoDesdeCarrito), que transforma el contenido del Carrito de un
 * usuario en un Pedido definitivo: copia cada ItemCarrito a un ItemPedido
 * (con el precio "congelado" al momento de la compra), descuenta el stock
 * de cada Producto vendido, calcula el total, y finalmente vacía el
 * carrito.
 *
 * Al estar todo dentro de un único método @Transactional, si algo falla a
 * mitad de camino (por ejemplo, no hay stock de un producto), TODA la
 * operación se revierte: no queda un pedido a medio crear ni stock
 * descontado de otros productos.
 */
@Service
@Transactional
public class PedidoService {

    private static final String ESTADO_PENDIENTE = "PENDIENTE";
    private static final String ESTADO_CANCELADO = "CANCELADO";

    private final PedidoRepository pedidoRepository;
    private final CarritoRepository carritoRepository;
    private final ProductoRepository productoRepository;

    public PedidoService(PedidoRepository pedidoRepository,
                          CarritoRepository carritoRepository,
                          ProductoRepository productoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.carritoRepository = carritoRepository;
        this.productoRepository = productoRepository;
    }

    // Convierte un ItemPedido en su DTO, calculando el subtotal
    private ItemPedidoDTO toItemDTO(ItemPedido item) {
        Double subtotal = item.getPrecioUnitario() * item.getCantidad();
        return new ItemPedidoDTO(
                item.getId(),
                item.getProducto().getId(),
                item.getProducto().getNombre(),
                item.getTalle(),
                item.getCantidad(),
                item.getPrecioUnitario(),
                subtotal
        );
    }

    // Convierte un Pedido completo (con sus items) en su DTO
    private PedidoDTO toDTO(Pedido pedido) {
        List<ItemPedidoDTO> items = pedido.getItems().stream()
                .map(this::toItemDTO)
                .toList();
        return new PedidoDTO(pedido.getId(), pedido.getUsuario().getId(), pedido.getFecha(), pedido.getEstado(), pedido.getTotal(), items);
    }

    // Lista todos los pedidos del sistema
    public List<PedidoDTO> getAllPedidos() {
        return pedidoRepository.findAll().stream()
                .map(this::toDTO)
                .toList();
    }

    // Busca un pedido por id; si no existe, 404
    public PedidoDTO getPedidoById(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el pedido con id " + id));
        return toDTO(pedido);
    }

    // Lista el historial de pedidos de un usuario puntual
    public List<PedidoDTO> getPedidosByUsuario(Long usuarioId) {
        return pedidoRepository.findByUsuarioId(usuarioId).stream()
                .map(this::toDTO)
                .toList();
    }

    /**
     * Confirma la compra: toma el carrito actual del usuario y lo
     * transforma en un Pedido.
     *
     * Pasos:
     *  1) Busca el carrito del usuario y valida que no esté vacío.
     *  2) Por cada ItemCarrito, valida stock disponible.
     *  3) Crea el Pedido y, por cada ItemCarrito, crea un ItemPedido
     *     "congelando" el precio actual del producto.
     *  4) Descuenta el stock vendido de cada Producto.
     *  5) Calcula el total del pedido.
     *  6) Vacía el carrito, ya que su contenido pasó a ser el Pedido.
     */
    public PedidoDTO crearPedidoDesdeCarrito(Long usuarioId) {
        Carrito carrito = carritoRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("El usuario con id " + usuarioId + " no tiene un carrito asociado"));

        if (carrito.getItems().isEmpty()) {
            throw new BusinessException("No se puede confirmar la compra: el carrito está vacío");
        }

        // Validación de stock de TODOS los items antes de modificar nada (evita descuentos parciales)
        for (ItemCarrito itemCarrito : carrito.getItems()) {
            Producto producto = itemCarrito.getProducto();
            if (producto.getStock() == null || producto.getStock() < itemCarrito.getCantidad()) {
                throw new BusinessException("Stock insuficiente para el producto '" + producto.getNombre() + "'");
            }
        }

        Pedido pedido = new Pedido();
        pedido.setUsuario(carrito.getUsuario());
        pedido.setFecha(LocalDateTime.now());
        pedido.setEstado(ESTADO_PENDIENTE);

        double total = 0.0;
        for (ItemCarrito itemCarrito : carrito.getItems()) {
            Producto producto = itemCarrito.getProducto();

            // Se crea el ItemPedido copiando los datos del ItemCarrito, "congelando" el precio actual
            ItemPedido itemPedido = new ItemPedido();
            itemPedido.setPedido(pedido);
            itemPedido.setProducto(producto);
            itemPedido.setTalle(itemCarrito.getTalle());
            itemPedido.setCantidad(itemCarrito.getCantidad());
            itemPedido.setPrecioUnitario(producto.getPrecio());
            pedido.getItems().add(itemPedido);

            total += producto.getPrecio() * itemCarrito.getCantidad();

            // Se descuenta el stock vendido
            producto.setStock(producto.getStock() - itemCarrito.getCantidad());
            productoRepository.save(producto); 
        }
        pedido.setTotal(total);

        Pedido guardado = pedidoRepository.save(pedido); 

        // El contenido del carrito ya se convirtió en pedido: se vacía
        carrito.getItems().clear();
        carritoRepository.save(carrito);

        return toDTO(guardado);
    }

    // Actualiza el estado de un pedido (ej: PENDIENTE -> ENVIADO -> ENTREGADO)
    public PedidoDTO actualizarEstado(Long id, String nuevoEstado) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el pedido con id " + id));
        pedido.setEstado(nuevoEstado.toUpperCase());
        Pedido actualizado = pedidoRepository.save(pedido);
        return toDTO(actualizado);
    }

    /**
     * Cancela un pedido: solo se permite si todavía está PENDIENTE, y al
     * cancelarlo se devuelve (restaura) el stock de cada producto vendido.
     */
    public PedidoDTO cancelarPedido(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el pedido con id " + id));

        if (!ESTADO_PENDIENTE.equalsIgnoreCase(pedido.getEstado())) {
            throw new BusinessException("Solo se pueden cancelar pedidos en estado PENDIENTE");
        }

        // Se restaura el stock de cada producto del pedido cancelado
        for (ItemPedido item : pedido.getItems()) {
            Producto producto = item.getProducto();
            producto.setStock(producto.getStock() + item.getCantidad());
            productoRepository.save(producto);
        }

        pedido.setEstado(ESTADO_CANCELADO);
        Pedido actualizado = pedidoRepository.save(pedido);
        return toDTO(actualizado);
    }
}
