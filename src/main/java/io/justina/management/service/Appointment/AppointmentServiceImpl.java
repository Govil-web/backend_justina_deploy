package io.justina.management.service.Appointment;

import io.justina.management.dto.appointment.AppointmentDataRegisterDTO;
import io.justina.management.dto.appointment.AppointmentResponseDTO;
import io.justina.management.exception.BadRequestException;
import io.justina.management.model.Appointment;
import io.justina.management.model.MedicalStaff;
import io.justina.management.model.Patient;
import io.justina.management.repository.AppointmentRepository;
import io.justina.management.repository.MedicalStaffRepository;
import io.justina.management.repository.PatientRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Implementación del servicio para gestionar citas médicas en el sistema.
 */
@Service
public class AppointmentServiceImpl implements AppointmentService {

    /**
     * Repositorio de citas médicas.
     */
    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final MedicalStaffRepository medicalStaffRepository;
    /**
     * ModelMapper para mapear entidades y DTO.
     */
    private final ModelMapper modelMapper = new ModelMapper();

    /**
     * Constructor que inicializa el servicio con el repositorio de citas médicas.
     *
     * @param appointmentRepository Repositorio de citas médicas.
     */
    @Autowired
    public AppointmentServiceImpl(AppointmentRepository appointmentRepository, MedicalStaffRepository medicalStaffRepository, PatientRepository patientRepository) {
        this.appointmentRepository = appointmentRepository;
        this.medicalStaffRepository = medicalStaffRepository;
        this.patientRepository = patientRepository;
    }
    /**
     * Registra una nueva cita médica en el sistema.
     *
     * @param appointmentData Datos de la cita médica que se desea registrar.
     * @return DTO con la información de la cita médica registrada.
     */
    @Transactional
    @Override
    public AppointmentResponseDTO registerAppointment(AppointmentDataRegisterDTO appointmentData) {
        // Retrieve Patient entity
        Patient patient = patientRepository.findById(appointmentData.getIdPatient())
                .orElseThrow(() -> new BadRequestException("Patient not found"));

        // Retrieve MedicalStaff entity
        MedicalStaff medicalStaff = medicalStaffRepository.findById(appointmentData.getIdMedicalStaff())
                .orElseThrow(() -> new BadRequestException("Medical staff not found"));

        // Map AppointmentDataRegisterDTO to Appointment entity
        Appointment appointment = modelMapper.map(appointmentData, Appointment.class);
        appointment.setPatient(patient);
        appointment.setMedicalStaff(medicalStaff);
        appointment.setActive(true);

        // Save Appointment entity
        appointment = appointmentRepository.save(appointment);

        // Map the saved Appointment entity to AppointmentResponseDTO
        AppointmentResponseDTO responseDTO = modelMapper.map(appointment, AppointmentResponseDTO.class);
        responseDTO.setIdPatient(patient.getId());
        responseDTO.setFullNamePatient(patient.getFirstName() + " " + patient.getLastName());
        responseDTO.setIdMedicalStaff(medicalStaff.getId());
        responseDTO.setFullNameMedicalStaff(medicalStaff.getFirstName() + " " + medicalStaff.getLastName());
        responseDTO.setSpecialty(medicalStaff.getSpecialities().toString());
        responseDTO.setHealthCenter(appointmentData.getHealthCenter());

        return responseDTO;
    }
    /**
     * Obtiene todas las citas médicas registradas en el sistema.
     *
     * @return Lista de todas las citas médicas registradas.
     */
    @Override
    public List<AppointmentResponseDTO> getAllAppointments() {
        List<Appointment> appointments = appointmentRepository.findAll();
        return getAppointmentResponseDTOS(appointments);
    }
    /**
     * Obtiene todas las citas médicas registradas en el sistema para un paciente específico.
     *
     * @param idPatient Identificador del paciente.
     * @return Lista de todas las citas médicas para el paciente especificado.
     */
    @Override
    public List<AppointmentResponseDTO> getAppointmentsByPatient(Long idPatient) {
        List<Appointment> appointments = appointmentRepository.findAllById(Collections.singleton(idPatient));
        return getAppointmentResponseDTOS(appointments);
    }
    /**
     * Obtiene todas las citas médicas registradas en el sistema para un médico específico.
     *
     * @param idDoctor Identificador del médico.
     * @return Lista de todas las citas médicas para el médico especificado.
     */
    @Override
    public List<AppointmentResponseDTO> getAppointmentsByMedicalStaff(Long idDoctor) {
        List<Appointment> appointments = appointmentRepository.findAllById(Collections.singleton(idDoctor));
        return getAppointmentResponseDTOS(appointments);
    }

    private List<AppointmentResponseDTO> getAppointmentResponseDTOS(List<Appointment> appointments) {
        List<AppointmentResponseDTO> responseDTOs = new ArrayList<>();

        for (Appointment appointment : appointments) {
            Patient patient = appointment.getPatient();
            MedicalStaff medicalStaff = appointment.getMedicalStaff();

            AppointmentResponseDTO responseDTO = modelMapper.map(appointment, AppointmentResponseDTO.class);
            responseDTO.setIdPatient(patient.getId());
            responseDTO.setFullNamePatient(patient.getFirstName() + " " + patient.getLastName());
            responseDTO.setIdMedicalStaff(medicalStaff.getId());
            responseDTO.setFullNameMedicalStaff(medicalStaff.getFirstName() + " " + medicalStaff.getLastName());
            responseDTO.setSpecialty(medicalStaff.getSpecialities().toString());
            responseDTO.setHealthCenter(appointment.getHealthCenter());

            responseDTOs.add(responseDTO);
        }

        return responseDTOs;
    }

    /**
     * Elimina una cita médica del sistema.
     *
     * @param idAppointment Identificador de la cita médica que se desea eliminar.
     */
    @Transactional
    @Override
    public void deleteAppointment(Long idAppointment) {
        appointmentRepository.deleteById(idAppointment);
    }
}
