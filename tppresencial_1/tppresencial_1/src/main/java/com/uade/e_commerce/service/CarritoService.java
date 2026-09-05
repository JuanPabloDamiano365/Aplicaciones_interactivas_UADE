package com.uade.e_commerce.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.uade.e_commerce.dto.AgregarItemCarritoDTO;
import com.uade.e_commerce.dto.CarritoDTO;
import com.uade.e_commerce.dto.ItemCarritoDTO;
import com.uade.e_commerce.exception.BusinessException;
import com.uade.e_commerce.exception.ResourceNotFoundException;
import com.uade.e_commerce.model.Carrito;
import com.uade.e_commerce.model.ItemCarrito;
import com.uade.e_commerce.model.Producto;
import com.uade.e_commerce.repository.CarritoRepository;
import com.uade.e_commerce.repository.ItemCarritoRepository;
import com.uade.e_commerce.repository.ProductoRepository;

/**
 * Capa de Lógica de Negocio (Service) de Carrito.
 *
 * Contiene la lógica de "agregar producto al carrito", que es más
 * compleja que un simple CRUD: valida que el producto exista y tenga
 * stock, y si el mismo producto+talle ya estaba en el carrito, suma la
 * cantidad en vez de crear un ítem duplicado.
 */
@Service
@Transactional
public class CarritoService {

    private final CarritoRepository carritoRepository;
    private final ItemCarritoRepository itemCarritoRepository;
    private final ProductoRepository productoRepository;

    public CarritoService(CarritoRepository carritoRepository,
                           ItemCarritoRepository itemCarritoRepository,
                           ProductoRepository productoRepository) {
        this.carritoRepository = carritoRepository;
        this.itemCarritoRepository = itemCarritoRepository;
        this.productoRepository = productoRepository;
    }

    // Convierte un ItemCarrito en su DTO, calculando el subtotal (precio * cantidad)
    private ItemCarritoDTO toItemDTO(ItemCarrito item) {
        Double subtotal = item.getProducto().getPrecio() * item.getCantidad();
        return new ItemCarritoDTO(
                item.getId(),
                item.getProducto().getId(),
                item.getProducto().getNombre(),
                item.getProducto().getPrecio(),
                item.getTalle(),
                item.getCantidad(),
                subtotal
        );
    }

    // Convierte el Carrito completo en su DTO, incluyendo la lista de items y el total acumulado
    private CarritoDTO toDTO(Carrito carrito) {
        List<ItemCarritoDTO> items = carrito.getItems().stream()
                .map(this::toItemDTO)
                .toList();
        Double total = items.stream().mapToDouble(ItemCarritoDTO::getSubtotal).sum();
        return new CarritoDTO(carrito.getId(), carrito.getUsuario().getId(), carrito.getFechaCreacion(), items, total);
    }

    // Busca el carrito de un usuario puntual (cada usuario tiene un único carrito)
    private Carrito buscarCarritoPorUsuario(Long usuarioId) {
        return carritoRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("El usuario con id " + usuarioId + " no tiene un carrito asociado"));
    }

    // GET: devuelve el carrito (con sus items) del usuario indicado
    public CarritoDTO getCarritoByUsuario(Long usuarioId) {
        return toDTO(buscarCarritoPorUsuario(usuarioId));
    }

    /**
     * Agrega un producto al carrito de un usuario.
     * - Valida que el producto exista.
     * - Valida que haya stock suficiente.
     * - Si ya existía un ítem con el mismo producto y talle, suma la
     *   cantidad en vez de crear un ítem nuevo.
     */
    public CarritoDTO agregarItem(Long usuarioId, AgregarItemCarritoDTO request) {
        Carrito carrito = buscarCarritoPorUsuario(usuarioId);

        Producto producto = productoRepository.findById(request.getProductoId())
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el producto con id " + request.getProductoId()));

        if (producto.getStock() == null || producto.getStock() < request.getCantidad()) {
            throw new BusinessException("Stock insuficiente para el producto '" + producto.getNombre() + "'");
        }

        // Busca si ya hay un item en el carrito con ese mismo producto y talle
        ItemCarrito item = itemCarritoRepository
                .findByCarritoIdAndProductoIdAndTalle(carrito.getId(), producto.getId(), request.getTalle())
                .orElse(null);

        if (item != null) {
            // Ya existía: se suma la cantidad pedida a la que ya tenía
            item.setCantidad(item.getCantidad() + request.getCantidad());
        } else {
            // No existía: se crea un ItemCarrito nuevo y se agrega a la lista del carrito
            item = new ItemCarrito();
            item.setCarrito(carrito);
            item.setProducto(producto);
            item.setTalle(request.getTalle());
            item.setCantidad(request.getCantidad());
            carrito.getItems().add(item);
        }

        itemCarritoRepository.save(item); 
        return toDTO(carrito);
    }

    // Elimina un ítem puntual del carrito (por ejemplo, el usuario se arrepiente de un producto)
    public CarritoDTO eliminarItem(Long usuarioId, Long itemId) {
        Carrito carrito = buscarCarritoPorUsuario(usuarioId);

        ItemCarrito item = itemCarritoRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el item de carrito con id " + itemId));

        // Chequeo de seguridad: el item debe pertenecer al carrito del usuario indicado
        if (!item.getCarrito().getId().equals(carrito.getId())) {
            throw new BusinessException("El item indicado no pertenece al carrito de este usuario");
        }

        carrito.getItems().remove(item); // orphanRemoval=true en Carrito.items -> se borra de la base de datos
        return toDTO(carrito);
    }

    // Vacía por completo el carrito de un usuario (por ejemplo, tras confirmar la compra)
    public void vaciarCarrito(Long usuarioId) {
        Carrito carrito = buscarCarritoPorUsuario(usuarioId);
        carrito.getItems().clear();
        carritoRepository.save(carrito);
    }
}
