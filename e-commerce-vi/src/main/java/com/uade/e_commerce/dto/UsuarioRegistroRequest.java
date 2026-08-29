package com.uade.e_commerce.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import java.time.LocalDate;

public record UsuarioRegistroRequest(
        @NotBlank String nombre,
        @NotBlank String apellido,
        @Email @NotBlank String email,
        @NotBlank String password,
        @NotNull @Past LocalDate fechaNacimiento,
        @NotBlank String sexo) {
}
