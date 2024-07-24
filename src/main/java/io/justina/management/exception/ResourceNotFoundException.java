package io.justina.management.exception;

/**
 * Excepción que indica que un recurso no fue encontrado.
 * Esta excepción se utiliza para indicar que un recurso solicitado no pudo ser encontrado en el sistema.
 */
public class ResourceNotFoundException extends RuntimeException {

    /**
     * Constructor que acepta un mensaje de error.
     *
     * @param message Mensaje que describe la causa de la excepción.
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }
}

