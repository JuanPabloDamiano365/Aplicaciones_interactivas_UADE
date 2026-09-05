package com.uade.e_commerce.exception;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Manejador global de excepciones de toda la API.
 *
 * @RestControllerAdvice hace que Spring intercepte, en un único lugar
 * centralizado, las excepciones que se lanzan en cualquier @RestController
 * de la aplicación, y las convierta en una respuesta HTTP con el código de
 * estado y el formato de body adecuados (en vez de que cada Controller
 * tenga que hacer try/catch a mano en cada método).
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Estructura común y prolija para todas las respuestas de error de la API
    private Map<String, Object> construirBody(HttpStatus status, String mensaje) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("mensaje", mensaje);
        return body;
    }

    // Se dispara cuando se lanza ResourceNotFoundException -> responde 404 NOT FOUND
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleNotFound(ResourceNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(construirBody(HttpStatus.NOT_FOUND, ex.getMessage()));
    }

    // Se dispara cuando se lanza BusinessException (regla de negocio violada) -> responde 400 BAD REQUEST
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Object> handleBusiness(BusinessException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(construirBody(HttpStatus.BAD_REQUEST, ex.getMessage()));
    }

    // Se dispara cuando fallan las validaciones de Bean Validation (@NotBlank, @NotNull, etc. en los DTOs)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidation(MethodArgumentNotValidException ex) {
        // Junta todos los mensajes de error de validación en un solo string legible
        String mensajes = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .reduce((a, b) -> a + " | " + b)
                .orElse("Datos inválidos");
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(construirBody(HttpStatus.BAD_REQUEST, mensajes));
    }

    // Cualquier otra excepción no controlada -> responde 500 INTERNAL SERVER ERROR
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGeneric(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(construirBody(HttpStatus.INTERNAL_SERVER_ERROR, "Ocurrió un error inesperado: " + ex.getMessage()));
    }
}
