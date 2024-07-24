package io.justina.management.controller.appointment;


import io.justina.management.model.Appointment;
import io.justina.management.service.Appointment.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @Autowired
    AppointmentService appointmentService;

    /**
     * Maneja la solicitud GET para obtener todas las citas médicas.
     *
     * @return ResponseEntity con la lista de citas médicas y el estado HTTP correspondiente.
     */
    @GetMapping()
    public ResponseEntity<List<Appointment>> findAll() {
        return new ResponseEntity<>(appointmentService.getAllAppointments(), HttpStatus.OK);
    }
}
