package io.justina.management.handler;


import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import io.justina.management.exception.ResourceNotFoundException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import io.justina.management.dto.error.ErrorDTO;

import java.time.LocalDateTime;

/**
 * Clase de manejo global de excepciones para controladores REST.
 * Esta clase utiliza la anotación {@code @RestControllerAdvice} para manejar excepciones lanzadas por controladores REST en toda la aplicación.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Maneja la excepción {@link ResourceNotFoundException} lanzada cuando un recurso no es encontrado.
     *
     * @param ex      La excepción de tipo {@link ResourceNotFoundException}.
     * @param request El objeto {@link WebRequest} que representa la solicitud web actual.
     * @return ResponseEntity con un objeto {@link ErrorDTO} que contiene detalles del error y código de estado HTTP {@link HttpStatus#NOT_FOUND}.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDTO> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        ErrorDTO errorDTO = new ErrorDTO(LocalDateTime.now(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDTO, HttpStatus.NOT_FOUND);
    }

}

