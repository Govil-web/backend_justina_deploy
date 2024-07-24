package io.justina.management.controller.medicalstaff;

import io.justina.management.dto.medicalstaff.MedicalStaffRegisterDTO;
import io.justina.management.dto.medicalstaff.MedicalStaffResponseDTO;
import io.justina.management.service.authentication.AuthenticationService;
import io.justina.management.service.medicalstaff.IMedicalStaffService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
/**
 * Controlador REST que maneja las operaciones relacionadas con el personal médico.
 *
 * <p>Este controlador maneja las solicitudes REST para operaciones CRUD sobre el personal médico,
 * utilizando la ruta base "/v1/api/medical-staff".
 */
@RestController
@RequestMapping("v1/api/medical-staff")
public class MedicalStaffController {

    private final IMedicalStaffService medicalStaffService;
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

    @PostMapping("/register")
    @Operation(summary = "Register a new medical staff")
    public ResponseEntity<MedicalStaffResponseDTO> registerMedicalStaff(@RequestBody @Valid MedicalStaffRegisterDTO medicalStaffRegisterDTO){
        MedicalStaffResponseDTO medicalStaff = medicalStaffService.registerMedicalStaff(medicalStaffRegisterDTO);
        return ResponseEntity.ok(medicalStaff);
    }

    /**
     * Maneja la solicitud GET para obtener todos los miembros del personal médico.
     *
     * @return ResponseEntity con la lista de personal médico y el estado HTTP correspondiente.
     */
    @GetMapping("/getAll")
    @Operation(summary = "Get all medical staff")
    public ResponseEntity<List<MedicalStaffResponseDTO>> getAllMedicalStaff(){
        return ResponseEntity.ok(medicalStaffService.getAllMedicalStaff());
    }
    /**
     * Maneja la solicitud GET para obtener todos los miembros activos del personal médico.
     *
     * @return ResponseEntity con la lista de personal médico activo y el estado HTTP correspondiente.
     */
    @GetMapping("/getActive")
    @Operation(summary = "Get all active medical staff")
    public ResponseEntity<List<MedicalStaffResponseDTO>> getMedicalStaffByActive(){
        return ResponseEntity.ok(medicalStaffService.getMedicalStaffByActive());
    }



    /**
     * Maneja la solicitud GET para obtener un miembro del personal médico por su ID.
     * Verifica si el usuario tiene acceso antes de devolver el resultado.
     *
     * @param id ID del personal médico que se desea obtener.
     * @return ResponseEntity con el personal médico encontrado y el estado HTTP correspondiente.
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get medical staff by id")
    public ResponseEntity<MedicalStaffResponseDTO> getMedicalStaffById(@PathVariable Long id){
        try {
            authenticationService.verifyUserAccess(id);
            return ResponseEntity.ok(medicalStaffService.getMedicalStaffById(id));
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
    /**
     * Maneja la solicitud DELETE para desactivar un miembro del personal médico por su ID.
     * Verifica si el usuario tiene acceso antes de desactivar al personal médico.
     *
     * @param id ID del personal médico que se desea desactivar.
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
