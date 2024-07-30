package io.justina.management.service.patient;


import io.justina.management.dto.patient.PatientRequestDTO;
import io.justina.management.dto.patient.PatientResponseDTO;
import io.justina.management.enums.RoleEnum;
import io.justina.management.model.Patient;
import io.justina.management.model.User;
import io.justina.management.repository.PatientRepository;
import io.justina.management.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Implementación del servicio para gestionar pacientes en el sistema.
 */
@Service
public class PatientServiceImpl implements PatientService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    private final PatientRepository patientRepository;

    @Autowired
    public PatientServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, PatientRepository patientRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = (BCryptPasswordEncoder) passwordEncoder;
        this.patientRepository = patientRepository;
    }
    ModelMapper modelMapper = new ModelMapper();
    /**
     * Obtiene todos los pacientes registrados en el sistema.
     *
     * @return Lista de todos los pacientes como objetos PatientResponseDTO.
     */
    @Override
    public List<PatientResponseDTO> getAllPatients() {
        modelMapper.typeMap(Patient.class, PatientResponseDTO.class)
                .addMappings(mapper -> {
                    mapper.map(src -> src.getUser().getFirstName(), PatientResponseDTO::setFirstName);
                    mapper.map(src -> src.getUser().getLastName(), PatientResponseDTO::setLastName);
                    mapper.map(src -> src.getUser().getEmail(), PatientResponseDTO::setEmail);
                    mapper.map(src -> src.getUser().getActive(), PatientResponseDTO::setActive);
                });
        return patientRepository.findAll().stream()
                .map(patient -> modelMapper.map(patient, PatientResponseDTO.class))
                .toList();
    }
    /**
        * Obtiene un paciente por su ID.
        *
        * @param patientId iD del paciente que se desea obtener.
        * @return Objeto Patient correspondiente al paciente encontrado.
        * @throws EntityNotFoundException Si no se encuentra un paciente con el ID especificado.
        */
    @Override
    public PatientResponseDTO getPatientById(Long patientId) {
        Optional<Patient> patientOptional = patientRepository.findById(patientId);
        if(patientOptional.isPresent()){
            Patient patient = patientOptional.get();
            PatientResponseDTO responseDTO = modelMapper.map(patient, PatientResponseDTO.class);
            responseDTO.setFirstName(patient.getUser().getFirstName());
            responseDTO.setLastName(patient.getUser().getLastName());
            responseDTO.setEmail(patient.getUser().getEmail());
            responseDTO.setIdentificationNumber(patient.getIdentificationNumber());
            responseDTO.setBirthDate(patient.getBirthDate());
            responseDTO.setBloodType(patient.getBloodType());
            responseDTO.setBloodFactor(patient.getBloodFactor());
            return responseDTO;
        } else {
            throw new IllegalArgumentException("Patient not found with id: " + patientId);

        }
    }
    /**
     * Crea un nuevo paciente en el sistema.
     *
     * @param patientRequestDTO Objeto que representa al paciente que se desea crear.
     * @return Objeto Patient creado.
     */
    @Override
    @Transactional
    public PatientResponseDTO createPatient(PatientRequestDTO patientRequestDTO) {
        User existingUser = userRepository.findByEmail(patientRequestDTO.getEmail());
        if (existingUser != null) {
            throw new RuntimeException("User already exists with email: " + patientRequestDTO.getEmail());
        }
        User user = modelMapper.map(patientRequestDTO, User.class);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoleEnum(RoleEnum.ROLE_PATIENT);
        user.setActive(true);
        userRepository.save(user);

        Patient existingPatient = patientRepository.findByUser(user);
        if (existingPatient != null) {
            throw new RuntimeException("Patient already exists for user with email: " + patientRequestDTO.getEmail());
        }
        Patient patient = modelMapper.map(patientRequestDTO, Patient.class);
        patient.setUser(user);
        patient.setBirthDate(LocalDate.parse(patientRequestDTO.getBirthDate()));
        patientRepository.save(patient);

        user.setPatient(patient);
        userRepository.save(user);

        PatientResponseDTO responseDTO = modelMapper.map(patient, PatientResponseDTO.class);
        responseDTO.setFirstName(user.getFirstName());
        responseDTO.setLastName(user.getLastName());
        responseDTO.setEmail(user.getEmail());

        return responseDTO;
    }


    /**
     * Desactiva a un paciente por su ID.
     *
     * @param patientId iD del paciente que se desea desactivar.
     * @throws EntityNotFoundException Si no se encuentra un paciente con el ID especificado.
     */
    @Override
    public void deactivatePatient(Long patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new EntityNotFoundException("Patient not found with id: " + patientId));
        patient.setActive(false);
        patientRepository.save(patient);
    }
}
