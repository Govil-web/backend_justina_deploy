package io.justina.management.dto.patient;

import lombok.Data;

import java.time.LocalDate;

/**
 * Clase DTO (Data Transfer Object) que representa la respuesta de información de un paciente.
 */
@Data
public class PatientResponseDTO {

    /**
     * Nombre del paciente.
     */
    private String firstName;
    /**
     * Apellido del paciente.
     */
    private String lastName;
    /**
     * Correo electrónico del paciente.
     */
    private String email;

    /**
     * Número de identificación del paciente.
     */
    private String identificationNumber;

    /**
     * Fecha de nacimiento del paciente.
     */
    private LocalDate birthDate;

    /**
     * Tipo de sangre del paciente.
     */
    private String bloodType;

    /**
     * Factor de sangre del paciente.
     */
    private String bloodFactor;


    /**
     * Sexo del paciente.
     */
    private char sex;
}
