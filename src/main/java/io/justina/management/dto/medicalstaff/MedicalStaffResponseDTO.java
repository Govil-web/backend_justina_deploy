package io.justina.management.dto.medicalstaff;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase DTO (Data Transfer Object) que representa la respuesta de información de un personal médico.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedicalStaffResponseDTO {

    /**
     * Identificador único del personal médico.
     */
    private Long id;

    /**
     * Nombre del personal médico.
     */
    private String firstName;

    /**
     * Apellido del personal médico.
     */
    private String lastName;

    /**
     * Correo electrónico del personal médico.
     */
    private String email;

    /**
     * Número de teléfono del personal médico.
     */
    private String phone;

    /**
     * Número de registro médico del personal.
     */
    private Integer medicalRegistrationNumber;

    /**
     * Especialidades médicas del personal.
     */
    private String specialities;

    /**
     * Descripción del perfil del personal médico.
     */
    private String description;
    /**
     * Estado activo/inactivo personal médico.
     */
    private Boolean active;
}

