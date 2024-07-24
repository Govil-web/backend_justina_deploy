package io.justina.management.exception;


/**
 * Excepción que indica una solicitud incorrecta.
 * Esta excepción se utiliza para indicar que la solicitud realizada por el cliente no es válida o está mal formada.
 */
public class BadRequestException extends RuntimeException {

    /**
     * Constructor que acepta un mensaje de error.
     *
     * @param message Mensaje que describe la causa de la excepción.
     */
    public BadRequestException(String message) {
        super(message);
    }
}
