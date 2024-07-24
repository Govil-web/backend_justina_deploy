package io.justina.management.repository;

import io.justina.management.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Interfaz de repositorio para la entidad Appointment.
 * Esta interfaz proporciona métodos para realizar operaciones de persistencia y consulta
 * sobre entidades de tipo Appointment en la base de datos.
 */
@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {

}
