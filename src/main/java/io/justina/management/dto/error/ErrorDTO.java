package io.justina.management.dto.error;

import java.io.Serializable;
import java.time.LocalDateTime;
/**
 * Clase que representa un objeto DTO para los errores.
 *
 * <p>Este objeto encapsula la información de un error, incluyendo la marca de tiempo del error,
 * el mensaje del error y los detalles adicionales.
 */
public record ErrorDTO(

        LocalDateTime timeStamp,

        String message,

        String details) implements Serializable {
}
