package com.uade.e_commerce.exception;

/**
 * Excepción propia que representa una violación de una regla de negocio
 * (por ejemplo: "no hay stock suficiente", "el carrito está vacío").
 *
 * El GlobalExceptionHandler la transforma en una respuesta HTTP 400
 * (Bad Request).
 */
public class BusinessException extends RuntimeException {

    public BusinessException(String mensaje) {
        super(mensaje);
    }
}
