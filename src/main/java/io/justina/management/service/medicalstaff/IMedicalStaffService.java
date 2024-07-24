package io.justina.management.service.medicalstaff;

import io.justina.management.dto.medicalstaff.MedicalStaffResponseDTO;
import io.justina.management.dto.medicalstaff.MedicalStaffRegisterDTO;

import java.util.List;

/**
 * Interfaz que define los métodos para gestionar el personal médico en el sistema.
 */
public interface IMedicalStaffService {

    /**
     * Registra un nuevo miembro del personal médico en el sistema.
     *
     * @param medicalStaffRegisterDTO DTO con los datos del personal médico que se desea registrar.
     * @return DTO que representa al personal médico registrado.
     */
    MedicalStaffResponseDTO registerMedicalStaff(MedicalStaffRegisterDTO medicalStaffRegisterDTO);

    /**
     * Obtiene un miembro del personal médico por su ID.
     *
     * @param id ID del miembro del personal médico que se desea obtener.
     * @return DTO que representa al miembro del personal médico encontrado.
     */
    MedicalStaffResponseDTO getMedicalStaffById(Long id);

    /**
     * Obtiene todos los miembros del personal médico registrados en el sistema.
     *
     * @return Lista de todos los miembros del personal médico como objetos MedicalStaffResponseDTO.
     */
    List<MedicalStaffResponseDTO> getAllMedicalStaff();

    /**
     * Desactiva a un miembro del personal médico por su ID.
     *
     * @param id ID del miembro del personal médico que se desea desactivar.
     */
    void deactivateMedicalStaff(Long id);

    /**
     * Obtiene todos los miembros del personal médico activos en el sistema.
     *
     * @return Lista de todos los miembros del personal médico activos como objetos MedicalStaffResponseDTO.
     */
    List<MedicalStaffResponseDTO> getMedicalStaffByActive();
}

