package io.justina.management.dto.error;

import java.time.LocalDateTime;
/**
 * Clase que representa un objeto DTO para los errores.
 *
 * <p>Este objeto encapsula la información de un error, incluyendo la marca de tiempo del error,
 * el mensaje del error y los detalles adicionales.
 */
public record ErrorDTO(
        /**
         * La marca de tiempo en la que ocurrió el error.
         */
        LocalDateTime timeStamp,

        /**
         * El mensaje descriptivo del error.
         */
        String message,

        /**
         * Detalles adicionales del error.
         */

        String details) {
}
