package io.justina.management.service.Appointment;

import io.justina.management.dto.appointment.AppointmentDataRegisterDTO;
import io.justina.management.dto.appointment.AppointmentResponseDTO;
import io.justina.management.exception.BadRequestException;
import io.justina.management.model.Appointment;
import io.justina.management.repository.AppointmentRepository;
import io.justina.management.repository.MedicalStaffRepository;
import io.justina.management.repository.PatientRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
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
        if(!patientRepository.existsById(appointmentData.getIdPatient())){
            throw new BadRequestException("El id del paciente no fue encontrado");
        }
        if(!medicalStaffRepository.existsById(appointmentData.getIdMedicalStaff())){
            throw new BadRequestException("El id del profesional no fue encontrado");
        }
        Appointment appointment = modelMapper.map(appointmentData, Appointment.class);
        appointment = appointmentRepository.save(appointment);
        return modelMapper.map(appointment, AppointmentResponseDTO.class);
    }

    /**
     * Obtiene todas las citas médicas registradas en el sistema.
     *
     * @return Lista de todas las citas médicas registradas.
     */
    @Override
    public List<AppointmentResponseDTO> getAllAppointments() {
        List<Appointment> appointments = appointmentRepository.findAll();
        Type listType = new TypeToken<List<AppointmentResponseDTO>>() {}.getType();
        return modelMapper.map(appointments, listType);
    }
    /**
     * Obtiene todas las citas médicas registradas en el sistema para un paciente específico.
     *
     * @param idPatient Identificador del paciente.
     * @return Lista de todas las citas médicas para el paciente especificado.
     */
    @Override
    public List<AppointmentResponseDTO> getAppointmentsByPatient(Long idPatient) {
        List<Appointment> appointments = appointmentRepository.findByPatient_Id(idPatient);
        Type listType = new TypeToken<List<AppointmentResponseDTO>>() {}.getType();
        return modelMapper.map(appointments, listType);
    }
    /**
     * Obtiene todas las citas médicas registradas en el sistema para un médico específico.
     *
     * @param idDoctor Identificador del médico.
     * @return Lista de todas las citas médicas para el médico especificado.
     */
    @Override
    public List<AppointmentResponseDTO> getAppointmentsByMedicalStaff(Long idDoctor) {
        List<Appointment> appointments = appointmentRepository.findByMedicalStaff_Id(idDoctor);
        Type listType = new TypeToken<List<AppointmentResponseDTO>>() {}.getType();
        return modelMapper.map(appointments, listType);
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
