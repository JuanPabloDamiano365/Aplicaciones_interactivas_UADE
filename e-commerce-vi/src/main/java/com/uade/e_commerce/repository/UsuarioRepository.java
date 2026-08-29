package com.uade.e_commerce.repository;

import com.uade.e_commerce.model.Usuario;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    boolean existsByEmailIgnoreCase(String email);
    Optional<Usuario> findByEmailIgnoreCase(String email);
}
