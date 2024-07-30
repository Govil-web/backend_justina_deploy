package io.justina.management.dto.appointment;

import io.justina.management.enums.Specialty;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
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
