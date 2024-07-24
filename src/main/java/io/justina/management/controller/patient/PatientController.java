package io.justina.management.controller.patient;

import io.justina.management.dto.patient.PatientRequestDTO;
import io.justina.management.dto.patient.PatientResponseDTO;
import io.justina.management.model.Patient;
import io.justina.management.service.patient.PatientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
/**
 * Controlador REST que maneja las operaciones relacionadas con los pacientes.
 *
 * <p>Este controlador maneja las solicitudes REST para operaciones CRUD sobre los pacientes,
 * utilizando la ruta base "/v1/api/patient".
 */
@RestController
@RequestMapping("v1/api/patient")
public class PatientController {

    @Autowired
    PatientService patientService;

    /**
     * Maneja la solicitud GET para obtener todos los pacientes.
     *
     * @return ResponseEntity con la lista de pacientes y el estado HTTP correspondiente.
     */
    @GetMapping()
    public ResponseEntity<List<PatientResponseDTO>> findAll() {
        List<PatientResponseDTO> patientList = patientService.getAllPatients();
        return new ResponseEntity<>(patientList, HttpStatus.OK);
    }

    /**
     * Maneja la solicitud GET para obtener un paciente por su ID.
     *
     * @param id ID del paciente que se desea obtener.
     * @return ResponseEntity con el paciente encontrado y el estado HTTP correspondiente.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Patient> findById(@PathVariable UUID id) {
        return new ResponseEntity<>(patientService.getPatientById(id), HttpStatus.OK);
    }
    /**
     * Maneja la solicitud POST para crear un nuevo paciente.
     *
     * @param patient Objeto del paciente que se desea crear.
     * @return ResponseEntity con el paciente creado y el estado HTTP correspondiente.
     */

    @PostMapping
    public ResponseEntity<PatientResponseDTO> createPatient(@RequestBody @Valid PatientRequestDTO patient) {
        PatientResponseDTO createdPatient = patientService.createPatient(patient);
        return new ResponseEntity<>(createdPatient, HttpStatus.CREATED);
    }
    /**
     * Maneja la solicitud DELETE para desactivar un paciente por su ID.
     *
     * @param id ID del paciente que se desea desactivar.
     * @return ResponseEntity con el estado HTTP correspondiente.
     */

    @DeleteMapping("/{id}")
    private ResponseEntity<Void> deletePatient(@PathVariable UUID id) {
        patientService.deactivatePatient(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }




}
