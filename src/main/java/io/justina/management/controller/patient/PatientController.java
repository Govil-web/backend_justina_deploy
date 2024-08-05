package io.justina.management.controller.patient;

import io.justina.management.dto.patient.PatientRequestDTO;
import io.justina.management.dto.patient.PatientResponseDTO;
import io.justina.management.service.authentication.AuthenticationService;
import io.justina.management.service.authentication.IAuthenticationService;
import io.justina.management.service.patient.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST que maneja las operaciones relacionadas con los pacientes.
 *
 * <p>Este controlador maneja las solicitudes REST para operaciones CRUD sobre los pacientes,
 * utilizando la ruta base "/v1/api/patient".
 */
@RestController
@RequestMapping("v1/api/patient")
public class PatientController {

    private final PatientService patientService;
    private final IAuthenticationService authenticationService;

    @Autowired
    public PatientController(PatientService patientService, IAuthenticationService authenticationService) {
            this.patientService = patientService;
            this.authenticationService = authenticationService;
        }
        /**
         * Maneja la solicitud GET para obtener todos los pacientes.
         *
         * @return ResponseEntity con la lista de pacientes y el estado HTTP correspondiente.
         */
        @Operation(summary = "Get all patients")
        @GetMapping()
        public ResponseEntity<List<PatientResponseDTO>> findAll () {
            List<PatientResponseDTO> patientList = patientService.getAllPatients();
            return new ResponseEntity<>(patientList, HttpStatus.OK);
        }

        /**
         * Maneja la solicitud GET para obtener un paciente por su ID.
         *
         * @param id id del paciente que se desea obtener.
         * @return ResponseEntity con el paciente encontrado y el estado HTTP correspondiente.
         */
        @Operation(summary = "Get a patient by ID")
        @GetMapping("/{id}")
        public ResponseEntity<PatientResponseDTO> findById (@PathVariable Long id){
            try {
                authenticationService.verifyUserAccess(id);
                return new ResponseEntity<>(patientService.getPatientById(id), HttpStatus.OK);
            }catch (AccessDeniedException e) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        }
        /**
         * Maneja la solicitud POST para crear un nuevo paciente.
         *
         * @param patient Objeto del paciente que se desea crear.
         * @return ResponseEntity con el paciente creado y el estado HTTP correspondiente.
         */
        @Operation(summary = "Create a new patient")
        @PostMapping
        public ResponseEntity<PatientResponseDTO> createPatient (@RequestBody @Valid PatientRequestDTO patient){
            PatientResponseDTO createdPatient = patientService.createPatient(patient);
            return new ResponseEntity<>(createdPatient, HttpStatus.CREATED);
        }
        /**
         * Maneja la solicitud DELETE para desactivar un paciente por su ID.
         *
         * @param id id del paciente que se desea desactivar.
         * @return ResponseEntity con el estado HTTP correspondiente.
         */
        @Operation(summary = "Delete a patient by ID")
        @DeleteMapping("/{id}")
        private ResponseEntity<Void> deletePatient (@PathVariable Long id){
            patientService.deactivatePatient(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }


    }
