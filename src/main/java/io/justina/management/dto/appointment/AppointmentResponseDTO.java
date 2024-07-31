package io.justina.management.dto.appointment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Clase que representa el objeto de transferencia de datos para la respuesta de una cita médica.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentResponseDTO {
    /**
     * Identificador de la cita médica.
     */
    private Long id;
    /**
     * Identificador del paciente.
     */
    private Long idPatient;
    /**
     * Nombre del paciente.
     */
    private String fullNamePatient;
    /**
     * Identificador del profesional.
     */
    private Long idMedicalStaff;
    /**
     * Nombre del profesional.
     */
    private String fullNameMedicalStaff;
    /**
     * Especialidad del profesional.
     */
    private String specialty;
    /**
     * Centro de salud.
     */
    private String healthCenter;
    /**
     * Fecha de la cita.
     */
    private LocalDateTime date;

}
