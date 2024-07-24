package io.justina.management.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase DTO (Data Transfer Object) que representa la respuesta de información de un usuario.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDataDTO {

    /**
     * Nombre del usuario.
     */
    private String firstName;

    /**
     * Apellido del usuario.
     */
    private String lastName;

    /**
     * Correo electrónico del usuario.
     */
    private String email;

    /**
     * Rol del usuario.
     */
    private String role;
}
