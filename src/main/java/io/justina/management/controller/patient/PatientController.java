package io.justina.management.controller.patient;

import io.justina.management.dto.apiresponse.ApiResponse;
import io.justina.management.dto.patient.PatientRequestDTO;
import io.justina.management.dto.patient.PatientResponseDTO;
import io.justina.management.model.Patient;
import io.justina.management.service.authentication.IAuthenticationService;
import io.justina.management.service.patient.PatientService;
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
 * Controlador REST que maneja las operaciones relacionadas con los pacientes.
 *
 * <p>Este controlador maneja las solicitudes REST para operaciones CRUD sobre los pacientes,
 * utilizando la ruta base "api/patient".
 */
@RestController
@RequestMapping("api/patient")
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
        @GetMapping
        public ResponseEntity<ApiResponse<PatientResponseDTO>> findAll () {
            try{
                Iterable<PatientResponseDTO> patientList = patientService.getAllPatients();
                return new ResponseEntity<>(new ApiResponse<>(true, "Patients found", patientList), HttpStatus.OK);
            }catch (AccessDeniedException e){
                return new ResponseEntity<>(new ApiResponse<>(false, "Usuario no autorizado", null), HttpStatus.UNAUTHORIZED);
            }

        }
        /**
         * Maneja la solicitud GET para obtener un paciente por su ID.
         *
         * @param id id del paciente que se desea obtener.
         * @return ResponseEntity con el paciente encontrado y el estado HTTP correspondiente.
         */
        @Operation(summary = "Get a patient by ID")
        @GetMapping("/{id}")
        public ResponseEntity<ApiResponse<PatientResponseDTO>> findById(@PathVariable Long id) {
            try {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                System.out.println("Controller: User authorities = " + authentication.getAuthorities());
                System.out.println("Controller: Requested patient ID = " + id);

                if (authentication.getAuthorities().stream()
                        .anyMatch(a -> a.getAuthority().equals("ROLE_PATIENT"))) {
                    Patient patient = (Patient) authentication.getPrincipal();
                    System.out.println("Controller: Authenticated patient ID = " + patient.getId());
                    if (!patient.getId().equals(id)) {
                        System.out.println("Controller: Access denied - Patient trying to access another patient's data");
                        return new ResponseEntity<>(new ApiResponse<>(false, "Access is denied", null), HttpStatus.FORBIDDEN);
                    }
                }

                boolean hasAccess = authenticationService.verifyUserAccess(id);
                System.out.println("Controller: verifyUserAccess result = " + hasAccess);

                if (!hasAccess) {
                    System.out.println("Controller: Access denied by verifyUserAccess");
                    return new ResponseEntity<>(new ApiResponse<>(false, "Access is denied", null), HttpStatus.FORBIDDEN);
                }

                PatientResponseDTO patientDTO = patientService.getPatientById(id);
                return new ResponseEntity<>(new ApiResponse<>(true, "Patient found", patientDTO), HttpStatus.OK);
            } catch (EntityNotFoundException e) {
                System.out.println("Controller: Patient not found - " + e.getMessage());
                return new ResponseEntity<>(new ApiResponse<>(false, "Patient not found", null), HttpStatus.NOT_FOUND);
            } catch (AccessDeniedException e) {
                System.out.println("Controller: Access denied - " + e.getMessage());
                return new ResponseEntity<>(new ApiResponse<>(false, "Access is denied", null), HttpStatus.FORBIDDEN);
            }
        }
        /**
         * Maneja la solicitud POST para crear un nuevo paciente.
         *
         * @param patient Objeto del paciente que se desea crear.
         * @return ResponseEntity con el paciente creado y el estado HTTP correspondiente.
         */
        @Operation(summary = "Create a new patient")
        @PostMapping("/add")
        public ResponseEntity<ApiResponse<PatientResponseDTO>> createPatient (@RequestBody @Valid PatientRequestDTO patient){
            try{
                if(patient == null){
                    return new ResponseEntity<>(new ApiResponse<>(false, "El paciente no puede ser nulo", null),HttpStatus.BAD_REQUEST);
                }
                PatientResponseDTO createdPatient = patientService.createPatient(patient);
                return new ResponseEntity<>(new ApiResponse<>(true, "Paciente creado con exito" , createdPatient), HttpStatus.CREATED);
            }catch (AccessDeniedException e){
                if(e.getMessage().equals("Access is denied")) {
                    return new ResponseEntity<>(new ApiResponse<>(false, "Usuario no autorizado", null), HttpStatus.UNAUTHORIZED);
                }
                return new ResponseEntity<>(new ApiResponse<>(false, "Usuario no autorizado", null), HttpStatus.UNAUTHORIZED);
            }
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
