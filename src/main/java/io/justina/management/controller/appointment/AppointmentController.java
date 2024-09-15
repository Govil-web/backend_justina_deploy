package io.justina.management.controller.appointment;


import io.justina.management.dto.apiresponse.ApiResponse;
import io.justina.management.dto.appointment.AppointmentDataRegisterDTO;
import io.justina.management.dto.appointment.AppointmentResponseDTO;
import io.justina.management.model.MedicalStaff;
import io.justina.management.model.Patient;
import io.justina.management.service.Appointment.AppointmentService;
import io.justina.management.service.authentication.IAuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


/**
 * Controlador REST que maneja las operaciones relacionadas con las citas médicas.
 *
 * <p>Este controlador maneja las solicitudes REST para operaciones CRUD sobre las citas médicas,
 * utilizando la ruta base "/v1/api/appointment".
 */

@RestController
@RequestMapping("api/appointment")
public class AppointmentController {

    /**
     * Servicio de citas médicas.
     */
    private final AppointmentService appointmentService;
    private final IAuthenticationService authenticationService;
    /**
     * Constructor que inicializa el controlador con el servicio de citas médicas.
     *
     * @param appointmentService Servicio de citas médicas.
     */
    @Autowired
    public AppointmentController(AppointmentService appointmentService, IAuthenticationService authenticationService) {
        this.appointmentService = appointmentService;
        this.authenticationService = authenticationService;
    }

    /**
     * Maneja la solicitud GET para obtener todas las citas médicas.
     *
     * @return ResponseEntity con la lista de citas médicas y el estado HTTP correspondiente.
     */
    @Operation(summary = "Get all appointments")
    @GetMapping
    public ResponseEntity<ApiResponse<AppointmentResponseDTO>> findAll() {
        try{
            Iterable<AppointmentResponseDTO> appointmentList = appointmentService.getAllAppointments();
            return new ResponseEntity<>(new ApiResponse<>(true, "Appointments found", appointmentList), HttpStatus.OK);
        }catch (AccessDeniedException e){
            return new ResponseEntity<>(new ApiResponse<>(false, "Usuario no autorizado", null), HttpStatus.UNAUTHORIZED);
        }
    }
    /**
     * Maneja la solicitud GET para obtener todas las citas médicas de un paciente.
     *
     * @param id Identificador del paciente.
     * @return ResponseEntity con la lista de citas médicas y el estado HTTP correspondiente.
     */
    @Operation(summary = "Get all appointments by patient")
    @GetMapping("/getByPatient/{id}")
    public ResponseEntity<ApiResponse<AppointmentResponseDTO>> getByPatient(@PathVariable Long id) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_PATIENT"))) {
                Patient patient = (Patient) authentication.getPrincipal();
                if (!patient.getId().equals(id)) {
                    return new ResponseEntity<>(new ApiResponse<>(false, "Usuario no autorizado", null), HttpStatus.UNAUTHORIZED);
                }

            }
            boolean hasAccess = authenticationService.verifyUserAccess(id);
            if (!hasAccess) {
                return new ResponseEntity<>(new ApiResponse<>(false, "Usuario no autorizado", null), HttpStatus.UNAUTHORIZED);
            }
            Iterable<AppointmentResponseDTO> appointmentList = appointmentService.getAppointmentsByPatient(id);
            return new ResponseEntity<>(new ApiResponse<>(true, "Appointments found", appointmentList), HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(new ApiResponse<>(false, "Usuario no autorizado", null), HttpStatus.NOT_FOUND);
        }catch (AccessDeniedException e){
            return new ResponseEntity<>(new ApiResponse<>(false, "Usuario no autorizado", null), HttpStatus.UNAUTHORIZED);
        }
    }
    /**
     * Maneja la solicitud GET para obtener todas las citas médicas de un médico.
     *
     * @param id Identificador del médico.
     * @return ResponseEntity con la lista de citas médicas y el estado HTTP correspondiente.
     */
    @Operation(summary = "Get all appointments by medical staff")
    @GetMapping("/getByMedicalStaff/{id}")
    public ResponseEntity<ApiResponse<AppointmentResponseDTO>> getByMedicalStaff(@PathVariable Long id) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("DOCTOR"))) {
                MedicalStaff medicalStaff = (MedicalStaff) authentication.getPrincipal();
                if (!medicalStaff.getId().equals(id)) {
                    return new ResponseEntity<>(new ApiResponse<>(false, "Usuario no autorizado", null), HttpStatus.UNAUTHORIZED);
                }
            }
            boolean hasAccess = authenticationService.verifyUserAccess(id);
            if (!hasAccess) {
                return new ResponseEntity<>(new ApiResponse<>(false, "Usuario no autorizado", null), HttpStatus.UNAUTHORIZED);
            }
            Iterable<AppointmentResponseDTO> appointmentList = appointmentService.getAppointmentsByMedicalStaff(id);
            return new ResponseEntity<>(new ApiResponse<>(true, "Appointments found", appointmentList), HttpStatus.OK);

        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(new ApiResponse<>(false, "Usuario no autorizado", null), HttpStatus.NOT_FOUND);
        }catch (AccessDeniedException e){
            return new ResponseEntity<>(new ApiResponse<>(false, "Usuario no autorizado", null), HttpStatus.UNAUTHORIZED);
        }
    }
    /**
     * Maneja la solicitud POST para registrar una nueva cita médica.
     *
     * @param appointment Datos de la cita médica a registrar.
     * @return ResponseEntity con la cita médica registrada y el estado HTTP correspondiente.
     */
    @Operation(summary="Register a new appointment")
    @PostMapping("/add")
    public ResponseEntity<ApiResponse<AppointmentResponseDTO>> register(@RequestBody @Valid AppointmentDataRegisterDTO appointment) {
        try {
            AppointmentResponseDTO appointmentDTO = appointmentService.registerAppointment(appointment);
            return new ResponseEntity<>(new ApiResponse<>(true, "Appointment created", appointmentDTO), HttpStatus.CREATED);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(new ApiResponse<>(false, "Patient or medical staff not found", null), HttpStatus.NOT_FOUND);
        } catch (AccessDeniedException e) {
            return new ResponseEntity<>(new ApiResponse<>(false, "Access is denied", null), HttpStatus.FORBIDDEN);
        }
    }

    /**
     * Maneja la solicitud DELETE para eliminar una cita médica.
     * @param id    Identificador de la cita médica a eliminar.
     * @return ResponseEntity con el estado HTTP correspondiente.
     */
    @Operation(summary = "Delete an appointment")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        appointmentService.deleteAppointment(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
