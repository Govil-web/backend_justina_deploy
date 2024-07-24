package io.justina.management.service.patient;


import io.justina.management.dto.patient.PatientRequestDTO;
import io.justina.management.dto.patient.PatientResponseDTO;
import io.justina.management.model.Patient;

import java.util.List;
import java.util.UUID;

/**
 * Interfaz que define los métodos para gestionar pacientes en el sistema.
 */
public interface PatientService {

    /**
     * Obtiene todos los pacientes registrados en el sistema.
     *
     * @return Lista de todos los pacientes como objetos PatientResponseDTO.
     */
    List<PatientResponseDTO> getAllPatients();

    /**
     * Obtiene un paciente por su ID.
     *
     * @param patientId ID del paciente que se desea obtener.
     * @return Objeto Patient correspondiente al paciente encontrado.
     */
    Patient getPatientById(UUID patientId);

    /**
     * Crea un nuevo paciente en el sistema.
     *
     * @param patient Objeto que representa al paciente que se desea crear.
     * @return Objeto Patient creado.
     */
    PatientResponseDTO createPatient(PatientRequestDTO patient);

    /**
     * Desactiva a un paciente por su ID.
     *
     * @param patientId ID del paciente que se desea desactivar.
     */
    void deactivatePatient(UUID patientId);

}

