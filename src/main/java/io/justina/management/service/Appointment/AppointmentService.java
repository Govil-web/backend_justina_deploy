package io.justina.management.service.Appointment;

import io.justina.management.model.Appointment;

import java.util.List;

/**
 * Interfaz que define el servicio para gestionar citas médicas en el sistema.
 */
public interface AppointmentService {

    /**
     * Obtiene todas las citas médicas registradas en el sistema.
     *
     * @return Lista de todas las citas médicas.
     */
    List<Appointment> getAllAppointments();
}

