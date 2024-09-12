package io.justina.management.dto.user;

import io.justina.management.dto.medicalstaff.MedicalStaffResponseDTO;
import io.justina.management.dto.patient.PatientResponseDTO;
import io.justina.management.model.MedicalStaff;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Clase DTO (Data Transfer Object) que representa la respuesta de información de un usuario.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDataDTO implements Serializable {

    /**
     * ID del usuario.
     */
    private Long id;

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
     * Numero de telefono del usuario.
     */
    private String phone;

    /**
     * Rol del usuario.
     */
    private String role;
    /**
     * Estado de activación del usuario.
     */
    private Boolean active;

}
