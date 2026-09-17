package com.uade.e_commerce.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActualizarEstadoDTO {

    @NotBlank(message = "Debe indicarse el nuevo estado")
    private String estado;
}
