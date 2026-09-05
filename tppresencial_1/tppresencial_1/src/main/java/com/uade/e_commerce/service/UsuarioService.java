package com.uade.e_commerce.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.uade.e_commerce.dto.UsuarioDTO;
import com.uade.e_commerce.exception.BusinessException;
import com.uade.e_commerce.exception.ResourceNotFoundException;
import com.uade.e_commerce.model.Carrito;
import com.uade.e_commerce.model.Usuario;
import com.uade.e_commerce.repository.CarritoRepository;
import com.uade.e_commerce.repository.UsuarioRepository;

/**
 * Capa de Lógica de Negocio (Service) de Usuario.
 *
 * Además del CRUD básico, al registrar un usuario nuevo se le crea
 * automáticamente su Carrito vacío asociado (regla de negocio: "todo
 * usuario tiene un carrito propio desde que se registra").
 */
@Service
@Transactional
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final CarritoRepository carritoRepository;

    public UsuarioService(UsuarioRepository usuarioRepository, CarritoRepository carritoRepository) {
        this.usuarioRepository = usuarioRepository;
        this.carritoRepository = carritoRepository;
    }

    // Convierte la entidad Usuario a DTO. Por seguridad, la contraseña NO se devuelve en las respuestas.
    private UsuarioDTO toDTO(Usuario usuario) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(usuario.getId());
        dto.setNombre(usuario.getNombre());
        dto.setEmail(usuario.getEmail());
        dto.setPassword(null); // Nunca se expone la contraseña en las respuestas de la API
        dto.setDireccion(usuario.getDireccion());
        dto.setTelefono(usuario.getTelefono());
        return dto;
    }

    // Lista todos los usuarios registrados
    public List<UsuarioDTO> getAllUsuarios() {
        return usuarioRepository.findAll().stream()
                .map(this::toDTO)
                .toList();
    }

    // Busca un usuario por id; si no existe, 404
    public UsuarioDTO getUsuarioById(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el usuario con id " + id));
        return toDTO(usuario);
    }

    /**
     * Registra un usuario nuevo. Valida que el email no esté ya usado y,
     * al persistir el Usuario, crea también su Carrito vacío asociado
     * (relación @OneToOne Usuario-Carrito).
     */
    public UsuarioDTO registrarUsuario(UsuarioDTO dto) {
        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new BusinessException("Ya existe un usuario registrado con el email " + dto.getEmail());
        }

        Usuario usuario = new Usuario();
        usuario.setNombre(dto.getNombre());
        usuario.setEmail(dto.getEmail());
        usuario.setPassword(dto.getPassword()); // En un sistema real, acá se debería encriptar (ej. BCrypt)
        usuario.setDireccion(dto.getDireccion());
        usuario.setTelefono(dto.getTelefono());

        Usuario guardado = usuarioRepository.save(usuario); 

        // Se crea automáticamente el carrito vacío del usuario recién registrado
        Carrito carrito = new Carrito();
        carrito.setUsuario(guardado);
        carrito.setFechaCreacion(LocalDateTime.now());
        carritoRepository.save(carrito); 

        return toDTO(guardado);
    }

    // Actualiza los datos de contacto de un usuario existente
    public UsuarioDTO actualizarUsuario(Long id, UsuarioDTO dto) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el usuario con id " + id));

        usuario.setNombre(dto.getNombre());
        usuario.setDireccion(dto.getDireccion());
        usuario.setTelefono(dto.getTelefono());
        // El email y la contraseña se dejan fuera de esta actualización simple para no
        // duplicar lógica de validación de unicidad de email / reseteo de contraseña.

        Usuario actualizado = usuarioRepository.save(usuario); 
        return toDTO(actualizado);
    }

    // Elimina un usuario (y, por cascada, su carrito y pedidos asociados)
    public void eliminarUsuario(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new ResourceNotFoundException("No se encontró el usuario con id " + id);
        }
        usuarioRepository.deleteById(id);
    }
}
