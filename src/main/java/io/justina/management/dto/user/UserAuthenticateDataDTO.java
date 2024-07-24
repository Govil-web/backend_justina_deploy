package io.justina.management.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase DTO (Data Transfer Object) que representa los datos de autenticación de un usuario.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAuthenticateDataDTO {

    /**
     * Correo electrónico del usuario para la autenticación.
     */
    @NotBlank(message = "El correo electrónico es obligatorio")
    @Email(message = "Debe ser una dirección de correo electrónico válida")
    private String email;

    /**
     * Contraseña del usuario para la autenticación.
     */
    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String password;
}
