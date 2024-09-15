package io.justina.management.controller.medicalstaff;

import io.justina.management.dto.apiresponse.ApiResponse;
import io.justina.management.dto.medicalstaff.MedicalStaffRegisterDTO;
import io.justina.management.dto.medicalstaff.MedicalStaffResponseDTO;
import io.justina.management.model.MedicalStaff;
import io.justina.management.service.authentication.AuthenticationService;
import io.justina.management.service.medicalstaff.IMedicalStaffService;
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
 * Controlador REST que maneja las operaciones relacionadas con el personal médico.
 *
 * <p>Este controlador maneja las solicitudes REST para operaciones CRUD sobre el personal médico,
 * utilizando la ruta base "/v1/api/medical-staff".
 */
@RestController
@RequestMapping("api/medical")
public class MedicalStaffController {

    /**
     * Servicio de personal médico que implementa la lógica de negocio.
     */
    private final IMedicalStaffService medicalStaffService;
    /**
     * Servicio de autenticación y autorización para verificar accesos.
     */
    private final AuthenticationService authenticationService;
    /**
     * Constructor que inyecta los servicios requeridos por el controlador.
     *
     * @param medicalStaffService Servicio de personal médico que implementa la lógica de negocio.
     * @param authenticationService Servicio de autenticación y autorización para verificar accesos.
     */
    @Autowired
    public MedicalStaffController(IMedicalStaffService medicalStaffService, AuthenticationService authenticationService){
        this.medicalStaffService = medicalStaffService;
        this.authenticationService = authenticationService;
    }
    /**
     * Maneja la solicitud POST para registrar un nuevo personal médico.
     *
     * @param medicalStaffRegisterDTO DTO que contiene la información del personal médico a registrar.
     * @return ResponseEntity con el personal médico registrado y el estado HTTP correspondiente.
     */

    @PostMapping("/add")
    @Operation(summary = "Register a new medical staff")
    public ResponseEntity<ApiResponse<MedicalStaffResponseDTO>> registerMedicalStaff(@RequestBody @Valid MedicalStaffRegisterDTO medicalStaffRegisterDTO){
        try{
            MedicalStaffResponseDTO medicalStaffDTO = medicalStaffService.registerMedicalStaff(medicalStaffRegisterDTO);
            return new ResponseEntity<>(new ApiResponse<>(true, "Medical staff registered", medicalStaffDTO), HttpStatus.CREATED);
        }catch (EntityNotFoundException e){
            return new ResponseEntity<>(new ApiResponse<>(false, "Medical staff not found", null), HttpStatus.NOT_FOUND);
        }

    }

    /**
     * Maneja la solicitud GET para obtener todos los miembros del personal médico.
     *
     * @return ResponseEntity con la lista de personal médico y el estado HTTP correspondiente.
     */
    @GetMapping
    @Operation(summary = "Get all medical staff")
    public ResponseEntity<ApiResponse<MedicalStaffResponseDTO>> getAllMedicalStaff(){
        try{
            Iterable<MedicalStaffResponseDTO> medicalStaffList = medicalStaffService.getAllMedicalStaff();
            return new ResponseEntity<>(new ApiResponse<>(true, "Medical staff found", medicalStaffList), HttpStatus.OK);
        }catch (AccessDeniedException e){
            return new ResponseEntity<>(new ApiResponse<>(false, "Access is denied", null), HttpStatus.UNAUTHORIZED);
        }
    }
    /**
     * Maneja la solicitud GET para obtener todos los miembros activos del personal médico.
     *
     * @return ResponseEntity con la lista de personal médico activo y el estado HTTP correspondiente.
     */
    @GetMapping("/getActive")
    @Operation(summary = "Get all active medical staff")
    public ResponseEntity<ApiResponse<MedicalStaffResponseDTO>> getMedicalStaffByActive(){
        try{
            Iterable<MedicalStaffResponseDTO> medicalStaffList = medicalStaffService.getMedicalStaffByActive();
            return new ResponseEntity<>(new ApiResponse<>(true, "Medical staff found", medicalStaffList), HttpStatus.OK);
        }catch (AccessDeniedException e){
            return new ResponseEntity<>(new ApiResponse<>(false, "Access is denied", null), HttpStatus.UNAUTHORIZED);
        }
    }



    /**
     * Maneja la solicitud GET para obtener un miembro del personal médico por su ID.
     * Verifica si el usuario tiene acceso antes de devolver el resultado.
     *
     * @param id id del personal médico que se desea obtener.
     * @return ResponseEntity con el personal médico encontrado y el estado HTTP correspondiente.
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get medical staff by id")
    public ResponseEntity <ApiResponse<MedicalStaffResponseDTO>> getMedicalStaffById(@PathVariable Long id) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_DOCTOR"))) {
                MedicalStaff medicalStaff = (MedicalStaff) authentication.getPrincipal();
                if (!medicalStaff.getId().equals(id)) {
                    return new ResponseEntity<>(new ApiResponse<>(false, "Access is denied", null), HttpStatus.FORBIDDEN);
                }
            }
            boolean hasAccess = authenticationService.verifyUserAccess(id);
            if (!hasAccess) {
                return new ResponseEntity<>(new ApiResponse<>(false, "Access is denied", null), HttpStatus.FORBIDDEN);
            }
            MedicalStaffResponseDTO medicalStaffDTO = medicalStaffService.getMedicalStaffById(id);
            return new ResponseEntity<>(new ApiResponse<>(true, "Medical staff found", medicalStaffDTO), HttpStatus.OK);
        }catch (EntityNotFoundException e) {
            System.out.println("Controller: Patient not found - " + e.getMessage());
            return new ResponseEntity<>(new ApiResponse<>(false, "Patient not found", null), HttpStatus.NOT_FOUND);
        } catch (AccessDeniedException e) {
            System.out.println("Controller: Access denied - " + e.getMessage());
            return new ResponseEntity<>(new ApiResponse<>(false, "Access is denied", null), HttpStatus.FORBIDDEN);
        }
    }
    /**
     * Maneja la solicitud DELETE para desactivar un miembro del personal médico por su ID.
     * Verifica si el usuario tiene acceso antes de desactivar al personal médico.
     *
     * @param id id del personal médico que se desea desactivar.
     * @return ResponseEntity con el estado HTTP correspondiente.
     */
    @DeleteMapping("delete/{id}")
    @Operation(summary = "Deactivate medical staff by id")
    public ResponseEntity<Void> deactivateMedicalStaff(@PathVariable Long id){
        try {
            authenticationService.verifyUserAccess(id);
            medicalStaffService.deactivateMedicalStaff(id);
            return ResponseEntity.ok().build();
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
