package io.justina.management.service.patient;


import io.justina.management.dto.patient.PatientRequestDTO;
import io.justina.management.dto.patient.PatientResponseDTO;
import io.justina.management.enums.RoleEnum;
import io.justina.management.model.Patient;
import io.justina.management.model.User;
import io.justina.management.repository.PatientRepository;
import io.justina.management.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

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
        // Mapeo personalizado para incluir el nombre del paciente desde el usuario asociado
        modelMapper.typeMap(Patient.class, PatientResponseDTO.class)
                .addMappings(mapper -> mapper.map(src -> src.getUser().getFirstName(), PatientResponseDTO::setFirstName));

        return patientRepository.findAll().stream()
                .map(patient -> modelMapper.map(patient, PatientResponseDTO.class))
                .toList();
    }

    /**
     * Obtiene un paciente por su ID.
     *
     * @param patientId    ID del paciente que se desea obtener.
     * @return Objeto Patient correspondiente al paciente encontrado.
     * @throws IllegalArgumentException Si no se encuentra un paciente con el ID especificado.
     */
    @Override
    public Patient getPatientById(UUID patientId) {
        return patientRepository.findById(patientId)
                .orElseThrow(() -> new IllegalArgumentException("Patient not found"));
    }

    /**
     * Crea un nuevo paciente en el sistema.
     *
     * @param patientRequestDTO Objeto que representa al paciente que se desea crear.
     * @return Objeto Patient creado.
     */
      @Override
      public PatientResponseDTO createPatient(PatientRequestDTO patientRequestDTO){
          var user = userRepository.findByEmail(patientRequestDTO.getEmail());
          if (user == null) {
              user = modelMapper.map(patientRequestDTO, User.class);
              user.setPassword(passwordEncoder.encode(user.getPassword()));
              user.setRoleEnum(RoleEnum.ROLE_PATIENT);
              user.setActive(true);
              userRepository.save(user);
          }
            Patient existingPatient = patientRepository.findByUser(user);
            if (existingPatient == null) {
                Patient patient = modelMapper.map(patientRequestDTO, Patient.class);
                patient.setUser(user);
                patient.setBirthDate(LocalDate.parse(patientRequestDTO.getBirthDate()));
                patientRepository.save(patient);
                user.setPatient(patient);
                userRepository.save(user);
            }
            Patient savedPatient = patientRepository.findByUser(user);
            PatientResponseDTO responseDTO = modelMapper.map(savedPatient, PatientResponseDTO.class);
            responseDTO.setFirstName(user.getFirstName());
            responseDTO.setLastName(user.getLastName());
            responseDTO.setEmail(user.getEmail());
            return responseDTO;
      }
//    @Override
//    public Patient createPatient(Patient patient) {
//        return patientRepository.save(patient);
//    }

    /**
     * Desactiva a un paciente por su ID.
     *
     * @param patientId ID del paciente que se desea desactivar.
     * @throws EntityNotFoundException Si no se encuentra un paciente con el ID especificado.
     */
    @Override
    public void deactivatePatient(UUID patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new EntityNotFoundException("Patient not found with id: " + patientId));
        patient.setActive(false);
        patientRepository.save(patient);
    }
}
