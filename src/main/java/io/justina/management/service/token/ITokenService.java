package io.justina.management.service.token;

import io.justina.management.model.User;
/**
 * Interfaz que define los métodos para la gestión de tokens de autenticación.
 */
public interface ITokenService {
    /**
     * Genera un token de autenticación para el usuario especificado.
     *
     * @param user Usuario para el cual se genera el token.
     * @return Token de autenticación generado.
     */
    String generateToken(User user);

    /**
     * Obtiene el sujeto (subject) del token especificado.
     *
     * @param token Token del cual se desea obtener el sujeto.
     * @return Sujeto (subject) del token.
     */
    String getSubject(String token);

}
