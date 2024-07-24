package io.justina.management.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase DTO (Data Transfer Object) que representa los datos de registro de un usuario.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterDataDTO {

    /**
     * Nombre del usuario.
     */
    @NotBlank(message = "El nombre es obligatorio")
    private String firstName;

    /**
     * Apellido del usuario.
     */
    @NotBlank(message = "El apellido es obligatorio")
    private String lastName;

    /**
     * Correo electrónico del usuario.
     */
    @NotBlank(message = "El correo electrónico es obligatorio")
    @Email(message = "Debe ser una dirección de correo electrónico válida")
    private String email;

    /**
     * Contraseña del usuario.
     */
    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String password;

    /**
     * Rol del usuario representado como un Enum.
     */
    @NotNull(message = "El rol es obligatorio")
    private String roleEnum;
}
