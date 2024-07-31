package io.justina.management.dto.appointment;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
/**
 * Clase que representa el objeto de transferencia de datos para el registro de una cita médica.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentDataRegisterDTO {

    /**
     * Identificador de la cita médica.
     */
    private Long id;
    /**
     * Identificador del paciente.
     */
    @NotNull(message = "El id del paciente no puede ser nulo")
    private Long idPatient;
    /**
     * Identificador del profesional.
     */
    @NotNull(message = "El id del profesional no puede ser nulo")
    private Long idMedicalStaff;
    /**
     * Motivo de la cita.
     */
    @NotNull(message = "El motivo no puede ser nulo")
    @Size(min = 1, max = 255, message = "El motivo debe tener entre 1 y 255 caracteres")
    private String reason;
    /**
     * Descripción de la cita.
     */
    @Size(max = 255, message = "La descripción debe tener hasta 255 caracteres")
    private String description;
    /**
     * Centro de salud.
     */
    private String healthCenter;
    /**
     * Fecha de la cita.
     */
    @NotNull(message = "La fecha no puede ser nula")
    @Future(message = "La fecha debe ser futura")
    private LocalDateTime date;


}
