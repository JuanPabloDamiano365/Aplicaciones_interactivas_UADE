package com.uade.e_commerce.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de Usuario. Se usa tanto para el registro (POST, donde se envía
 * "password" en texto plano) como para las respuestas (GET), en las que
 * el Service se encarga de vaciar el campo "password" antes de devolver
 * el DTO, para no exponer contraseñas por la API.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO {

    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email no tiene un formato válido")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    private String password;

    private String direccion;
    private String telefono;
}
