package io.justina.management.service.medicalstaff;

import io.justina.management.dto.medicalstaff.MedicalStaffRegisterDTO;
import io.justina.management.dto.medicalstaff.MedicalStaffResponseDTO;
import io.justina.management.enums.RoleEnum;
import io.justina.management.model.MedicalStaff;
import io.justina.management.model.User;
import io.justina.management.repository.MedicalStaffRepository;
import io.justina.management.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
/**
 * Servicio para la gestión del personal médico.
 * Implementa la interfaz IMedicalStaffService.
 */

@Service
public class MedicalStaffService implements IMedicalStaffService{

    private final MedicalStaffRepository medicalStaffRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    /**
     * Constructor para la clase MedicalStaffService.
     *
     * @param medicalStaffRepository Repositorio de personal médico
     * @param userRepository Repositorio de usuarios
     * @param passwordEncoder Codificador de contraseñas
     */
    @Autowired
    public MedicalStaffService(MedicalStaffRepository medicalStaffRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.medicalStaffRepository = medicalStaffRepository;
        this.passwordEncoder = (BCryptPasswordEncoder) passwordEncoder;
        this.userRepository = userRepository;
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
        var user = userRepository.findByEmail(medicalStaffRegisterDTO.getEmail());
        if (user == null) {
            user = modelMapper.map(medicalStaffRegisterDTO, User.class);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRoleEnum(RoleEnum.ROLE_DOCTOR);
            user.setActive(true);
            userRepository.save(user);
        }
        MedicalStaff existingMedicalStaff = medicalStaffRepository.findByUser(user);
        if (existingMedicalStaff == null) {
            MedicalStaff medicalStaff = modelMapper.map(medicalStaffRegisterDTO, MedicalStaff.class);
            medicalStaff.setUser(user);
            medicalStaffRepository.save(medicalStaff);
            user.setMedicalStaff(medicalStaff);
            userRepository.save(user);
        }
        MedicalStaff savedMedicalStaff = medicalStaffRepository.findByUser(user);
        MedicalStaffResponseDTO responseDTO = modelMapper.map(savedMedicalStaff, MedicalStaffResponseDTO.class);
        responseDTO.setFirstName(user.getFirstName());
        responseDTO.setLastName(user.getLastName());
        responseDTO.setEmail(user.getEmail());
        return responseDTO;
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
        Optional<MedicalStaff> medicalStaffOpt = medicalStaffRepository.findById(id);
        if (medicalStaffOpt.isPresent()) {
            MedicalStaff medicalStaff = medicalStaffOpt.get();
            MedicalStaffResponseDTO responseDTO = modelMapper.map(medicalStaff, MedicalStaffResponseDTO.class);
            User user = medicalStaff.getUser();
            responseDTO.setFirstName(user.getFirstName());
            responseDTO.setLastName(user.getLastName());
            responseDTO.setEmail(user.getEmail());
            return responseDTO;
        } else {
            throw new RuntimeException("Medical Staff not found with id: " + id);
        }
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
        List<MedicalStaff> medicalStaffs = medicalStaffRepository.findByUser_ActiveTrue();
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

        User user = medicalStaff.getUser();
        if (user != null) {
            user.setActive(false);
            userRepository.save(user);
        } else {
            throw new RuntimeException("User not found for Medical Staff with id: " + id);
        }
    }

    /**
     * Convierte una lista de objetos MedicalStaff en una lista de DTO MedicalStaffResponseDTO.
     *
     * @param medicalStaffs Lista de objetos MedicalStaff
     * @return Lista de DTOs MedicalStaffResponseDTO
     */
    private List<MedicalStaffResponseDTO> getMedicalStaffResponseDTOS(List<MedicalStaff> medicalStaffs) {
        return medicalStaffs.stream()
                .map(medicalStaff -> {
                    MedicalStaffResponseDTO dto = modelMapper.map(medicalStaff, MedicalStaffResponseDTO.class);
                    User user = medicalStaff.getUser();
                    if (user != null) {
                        dto.setFirstName(user.getFirstName());
                        dto.setLastName(user.getLastName());
                        dto.setEmail(user.getEmail());
                    }
                    return dto;
                })
                .collect(Collectors.toList());
    }

}
