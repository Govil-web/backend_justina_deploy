package io.justina.management.service.patient;


import io.justina.management.config.mapper.ModelMapperConfig;
import io.justina.management.dto.patient.PatientRequestDTO;
import io.justina.management.dto.patient.PatientResponseDTO;
import io.justina.management.enums.RoleEnum;
import io.justina.management.model.Patient;
import io.justina.management.repository.PatientRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Implementación del servicio para gestionar pacientes en el sistema.
 */
@Service
public class PatientServiceImpl implements PatientService {



    private final BCryptPasswordEncoder passwordEncoder;

    private final PatientRepository patientRepository;

    private final ModelMapperConfig modelMapperConfig;

    @Autowired
    public PatientServiceImpl(PasswordEncoder passwordEncoder,
                              PatientRepository patientRepository, ModelMapperConfig modelMapperConfig) {

        this.passwordEncoder = (BCryptPasswordEncoder) passwordEncoder;
        this.patientRepository = patientRepository;
        this.modelMapperConfig = modelMapperConfig;
    }
    /**
     * Obtiene todos los pacientes registrados en el sistema.
     *
     * @return Lista de todos los pacientes como objetos PatientResponseDTO.
     */
    @Override
    public List<PatientResponseDTO> getAllPatients() {
        List<Patient> patients = patientRepository.findAll();
        return patients.stream()
                .map(patient -> modelMapperConfig.modelMapperPatient().map(patient, PatientResponseDTO.class))
                .toList();
    }
    /**
        * Obtiene un paciente por su ID.
        *
        * @param patientId iD del paciente que se desea obtener.
        * @return Objeto Patient correspondiente al paciente encontrado.
        * @throws EntityNotFoundException Si no se encuentra un paciente con el ID especificado.
        */
//    @Override
//    public PatientResponseDTO getPatientById(Long patientId) {
//        Patient patient = patientRepository.findByPatient_Id(patientId);
//
//        PatientResponseDTO responseDTO = modelMapper.map(patient, PatientResponseDTO.class);
//        responseDTO.setId(patient.getUser().getId());
//        responseDTO.setFirstName(patient.getUser().getFirstName());
//        responseDTO.setLastName(patient.getUser().getLastName());
//        responseDTO.setEmail(patient.getUser().getEmail());
//        responseDTO.setIdentificationNumber(patient.getIdentificationNumber());
//        responseDTO.setBirthDate(patient.getBirthDate());
//        responseDTO.setBloodType(patient.getBloodType());
//        responseDTO.setBloodFactor(patient.getBloodFactor());
//
//        return responseDTO;
//    }

    @Override
    public PatientResponseDTO getPatientById(Long patientId) {
        Optional<Patient> patient = patientRepository.findById(patientId);
        return modelMapperConfig.modelMapperPatient().map(patient, PatientResponseDTO.class);
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
        Patient patient = modelMapperConfig.modelMapperPatient().map(patientRequestDTO, Patient.class);
        patient.setPassword(passwordEncoder.encode(patientRequestDTO.getPassword()));
        patient.setActive(true);
        patient.setRoleEnum(RoleEnum.ROLE_PATIENT);
        patient = patientRepository.save(patient);
        return modelMapperConfig.modelMapperPatient().map(patient, PatientResponseDTO.class);
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
