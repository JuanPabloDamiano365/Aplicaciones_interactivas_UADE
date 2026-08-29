package com.uade.e_commerce.dto;

import com.uade.e_commerce.model.Usuario;
import java.time.LocalDate;

public record UsuarioResponse(
        Long id,
        String nombre,
        String apellido,
        String email,
        LocalDate fechaNacimiento,
        String sexo) {

    public static UsuarioResponse from(Usuario usuario) {
        return new UsuarioResponse(usuario.getId(), usuario.getNombre(), usuario.getApellido(),
                usuario.getEmail(), usuario.getFechaNacimiento(), usuario.getSexo());
    }
}
