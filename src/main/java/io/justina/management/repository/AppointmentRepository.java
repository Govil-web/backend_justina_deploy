package io.justina.management.repository;


import io.justina.management.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Interfaz de repositorio para la entidad Appointment.
 * Esta interfaz proporciona métodos para realizar operaciones de persistencia y consulta
 * sobre entidades de tipo Appointment en la base de datos.
 */
@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

//    /**
//     * Busca y devuelve una lista de todas las citas médicas asociadas al paciente con el ID especificado.
//     *
//     * @param idPatient id del paciente asociado a las citas médicas.
//     * @return Lista de todas las citas médicas asociadas al paciente con el ID especificado.
//     */
    //List<Appointment> findByPatient_Id(Long idPatient);
//    /**
//     * Busca y devuelve una lista de todas las citas médicas asociadas al médico con el ID especificado.
//     *
//     * @param  del médico asociado a las citas médicas.
//     * @return Lista de todas las citas médicas asociadas al médico con el ID especificado.
//     */
    //List<Appointment> findByMedicalStaff_Id(Long idDoctor);
    @Query("SELECT a FROM Appointment a WHERE a.medicalStaff.user.id = :userId OR a.patient.user.id = :userId")
    List<Appointment> findByUser_Id(@Param("userId") Long userId);
}
