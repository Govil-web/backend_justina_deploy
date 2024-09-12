package io.justina.management.service.medicalstaff;

import io.justina.management.dto.medicalstaff.MedicalStaffRegisterDTO;
import io.justina.management.dto.medicalstaff.MedicalStaffResponseDTO;
import io.justina.management.enums.RoleEnum;
import io.justina.management.exception.BadRequestException;
import io.justina.management.model.MedicalStaff;
import io.justina.management.repository.MedicalStaffRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
/**
 * Servicio para la gestión del personal médico.
 * Implementa la interfaz IMedicalStaffService.
 */

@Service
public class MedicalStaffService implements IMedicalStaffService{

    private final MedicalStaffRepository medicalStaffRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    /**
     * Constructor para la clase MedicalStaffService.
     *
     * @param medicalStaffRepository Repositorio de personal médico
     * @param passwordEncoder Codificador de contraseñas
     */
    @Autowired
    public MedicalStaffService(MedicalStaffRepository medicalStaffRepository, PasswordEncoder passwordEncoder) {
        this.medicalStaffRepository = medicalStaffRepository;
        this.passwordEncoder = (BCryptPasswordEncoder) passwordEncoder;

    }

    private final ModelMapper modelMapper = new ModelMapper();

    /**
     * Registra un nuevo personal médico en el sistema.
     *
     * @param medicalStaffRegisterDTO DTO con la información del personal médico a registrar
     * @return DTO con la información del personal médico registrado
     */
    @Override
    @Transactional
    public MedicalStaffResponseDTO registerMedicalStaff(MedicalStaffRegisterDTO medicalStaffRegisterDTO) {
        MedicalStaff medicalStaff = modelMapper.map(medicalStaffRegisterDTO, MedicalStaff.class);
        medicalStaff.setPassword(passwordEncoder.encode(medicalStaffRegisterDTO.getPassword()));
        medicalStaff.setRoleEnum(RoleEnum.valueOf("DOCTOR"));
        medicalStaff.setActive(true);
        medicalStaff = medicalStaffRepository.save(medicalStaff);
        return modelMapper.map(medicalStaff, MedicalStaffResponseDTO.class);
    }
    /**
     * Obtiene la información de un miembro del personal médico por su ID.
     *
     * @param id ID del personal médico
     * @return DTO con la información del personal médico encontrado
     * @throws RuntimeException Si no se encuentra ningún personal médico con el ID especificado
     */
    @Override
    public MedicalStaffResponseDTO getMedicalStaffById(Long id) {
        MedicalStaff medicalStaff = medicalStaffRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Medical Staff not found with id: " + id));
        return modelMapper.map(medicalStaff, MedicalStaffResponseDTO.class);
    }
    /**
     * Obtiene la lista de todo el personal médico registrado.
     *
     * @return Lista de DTOs con la información de todo el personal médico
     */
    @Override
    public List<MedicalStaffResponseDTO> getAllMedicalStaff() {
        List<MedicalStaff> medicalStaffs = medicalStaffRepository.findAll();
        return getMedicalStaffResponseDTOS(medicalStaffs);
    }

    /**
     * Obtiene la lista de todo el personal médico activo.
     *
     * @return Lista de DTOs con la información de todo el personal médico activo
     */
    @Override
    public List<MedicalStaffResponseDTO> getMedicalStaffByActive() {
        List<MedicalStaff> medicalStaffs = medicalStaffRepository.findByActiveTrue();
        return getMedicalStaffResponseDTOS(medicalStaffs);
    }
    /**
     * Desactiva el personal médico especificado por su ID.
     *
     * @param id ID del personal médico a desactivar
     * @throws RuntimeException Si no se encuentra ningún personal médico con el ID especificado
     */
    @Transactional
    @Override
    public void deactivateMedicalStaff(Long id) {
        MedicalStaff medicalStaff = medicalStaffRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medical Staff not found with id: " + id));
        medicalStaff.setActive(false);
    }

    /**
     * Convierte una lista de objetos MedicalStaff en una lista de DTO MedicalStaffResponseDTO.
     *
     * @param medicalStaffs Lista de objetos MedicalStaff
     * @return Lista de DTOs MedicalStaffResponseDTO
     */
    private List<MedicalStaffResponseDTO> getMedicalStaffResponseDTOS(List<MedicalStaff> medicalStaffs) {
        return medicalStaffs.stream()
                .map(medicalStaff -> modelMapper.map(medicalStaff, MedicalStaffResponseDTO.class))
                .collect(Collectors.toList());
    }

}
