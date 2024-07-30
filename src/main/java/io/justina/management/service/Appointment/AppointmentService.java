package io.justina.management.service.Appointment;

import io.justina.management.dto.appointment.AppointmentDataRegisterDTO;
import io.justina.management.dto.appointment.AppointmentResponseDTO;

import java.util.List;

/**
 * Interfaz que define el servicio para gestionar citas médicas en el sistema.
 */
public interface AppointmentService {

    /**
     * Registra una nueva cita médica en el sistema.
     *
     * @param appointmentData DTO con los datos de la cita médica que se desea registrar.
     * @return DTO que representa la cita médica registrada.
     */
    AppointmentResponseDTO registerAppointment(AppointmentDataRegisterDTO appointmentData);

    /**
     * Obtiene todas las citas médicas registradas en el sistema.
     *
     * @return Lista de todas las citas médicas.
     */
    List<AppointmentResponseDTO> getAllAppointments();

    /**
     * Obtiene todas las citas médicas registradas en el sistema para un paciente específico.
     *
     * @param idPatient Identificador del paciente.
     * @return Lista de todas las citas médicas para el paciente especificado.
     */
    List<AppointmentResponseDTO> getAppointmentsByPatient(Long idPatient);
    /**
     * Obtiene todas las citas médicas registradas en el sistema para un médico específico.
     *
     * @param idDoctor Identificador del médico.
     * @return Lista de todas las citas médicas para el médico especificado.
     */
    List<AppointmentResponseDTO> getAppointmentsByMedicalStaff(Long idDoctor);

    /**
     * Elimina una cita médica del sistema.
     * @param idAppointment Identificador de la cita médica que se desea eliminar.
     */
    void deleteAppointment(Long idAppointment);

}

