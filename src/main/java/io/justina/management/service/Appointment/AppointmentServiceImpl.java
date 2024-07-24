package io.justina.management.service.Appointment;

import io.justina.management.model.Appointment;
import io.justina.management.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementación del servicio para gestionar citas médicas en el sistema.
 */
@Service
public class AppointmentServiceImpl implements AppointmentService {

    @Autowired
    AppointmentRepository appointmentRepository;

    /**
     * Obtiene todas las citas médicas registradas en el sistema.
     *
     * @return Lista de todas las citas médicas registradas.
     */
    @Override
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }
}
