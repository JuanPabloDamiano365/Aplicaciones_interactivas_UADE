package com.uade.e_commerce.exception;

/**
 * Excepción propia (unchecked, extiende RuntimeException) que se lanza
 * cuando se busca una entidad por id (Producto, Categoria, Usuario,
 * Carrito, Pedido) y no existe en la base de datos.
 *
 * La captura el GlobalExceptionHandler para transformarla en una
 * respuesta HTTP 404 (Not Found) con un mensaje claro para el cliente de
 * la API, en vez de dejar que Spring devuelva un error 500 genérico.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String mensaje) {
        super(mensaje);
    }
}
