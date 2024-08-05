package io.justina.management.controller.appointment;


import io.justina.management.dto.appointment.AppointmentDataRegisterDTO;
import io.justina.management.dto.appointment.AppointmentResponseDTO;
import io.justina.management.service.Appointment.AppointmentService;
import io.justina.management.service.authentication.IAuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
/**
 * Controlador REST que maneja las operaciones relacionadas con las citas médicas.
 *
 * <p>Este controlador maneja las solicitudes REST para operaciones CRUD sobre las citas médicas,
 * utilizando la ruta base "/v1/api/appointment".
 */

@RestController
@RequestMapping("v1/api/appointment")
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
    @GetMapping("/getAll")
    public ResponseEntity<List<AppointmentResponseDTO>> findAll() {
        return new ResponseEntity<>(appointmentService.getAllAppointments(), HttpStatus.OK);
    }
    /**
     * Maneja la solicitud GET para obtener todas las citas médicas de un paciente.
     *
     * @param idPatient Identificador del paciente.
     * @return ResponseEntity con la lista de citas médicas y el estado HTTP correspondiente.
     */
    @Operation(summary = "Get all appointments by patient")
    @GetMapping("/getByPatient/{idPatient}")
    public ResponseEntity<List<AppointmentResponseDTO>> getByPatient(@PathVariable Long idPatient) {
        try{
            authenticationService.verifyUserAccess(idPatient);
            return new ResponseEntity<>(appointmentService.getAppointmentsByPatient(idPatient), HttpStatus.OK);
        }catch (AccessDeniedException e){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
    /**
     * Maneja la solicitud GET para obtener todas las citas médicas de un médico.
     *
     * @param idDoctor Identificador del médico.
     * @return ResponseEntity con la lista de citas médicas y el estado HTTP correspondiente.
     */
    @Operation(summary = "Get all appointments by medical staff")
    @GetMapping("/getByMedicalStaff/{idDoctor}")
    public ResponseEntity<List<AppointmentResponseDTO>> getByMedicalStaff(@PathVariable Long idDoctor) {
        try{
            authenticationService.verifyUserAccess(idDoctor);
            return new ResponseEntity<>(appointmentService.getAppointmentsByMedicalStaff(idDoctor), HttpStatus.OK);
        }catch (AccessDeniedException e){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
    /**
     * Maneja la solicitud POST para registrar una nueva cita médica.
     *
     * @param appointment Datos de la cita médica a registrar.
     * @return ResponseEntity con la cita médica registrada y el estado HTTP correspondiente.
     */
    @Operation(summary="Register a new appointment")
    @PostMapping("/register")
    public ResponseEntity<AppointmentResponseDTO> register(@RequestBody @Valid AppointmentDataRegisterDTO appointment) {
        return new ResponseEntity<>(appointmentService.registerAppointment(appointment), HttpStatus.CREATED);
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
