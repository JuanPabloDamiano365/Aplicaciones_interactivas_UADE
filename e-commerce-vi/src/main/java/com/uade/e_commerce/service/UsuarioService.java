package com.uade.e_commerce.service;

import com.uade.e_commerce.exceptions.ArgumentInvalidException;
import com.uade.e_commerce.exceptions.ResourceNotFoundException;
import com.uade.e_commerce.dto.UsuarioRegistroRequest;
import com.uade.e_commerce.model.Usuario;
import com.uade.e_commerce.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
@Transactional
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Usuario registrar(UsuarioRegistroRequest request) {
        if (usuarioRepository.existsByEmailIgnoreCase(request.email())) {
            throw new ArgumentInvalidException("Ya existe un usuario con ese email");
        }
        Usuario usuario = new Usuario(null, request.nombre(), request.apellido(), request.email(),
                request.password(), request.fechaNacimiento(), request.sexo());
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        return usuarioRepository.save(usuario);
    }

    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado: " + id));
    }

    public List<Usuario> listar() {
        return usuarioRepository.findAll();
    }
}
