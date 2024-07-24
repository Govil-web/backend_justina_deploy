package io.justina.management.controller.financier;

import io.justina.management.dto.financier.FinancierRegisterDTO;
import io.justina.management.dto.financier.FinancierResponseDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.justina.management.service.financier.IFinancierService;

import java.util.List;
import java.util.UUID;
/**
 * Controlador REST que maneja las operaciones relacionadas con los financieros.
 *
 * <p>Este controlador maneja las solicitudes REST para operaciones CRUD sobre los financieros,
 * utilizando rutas bajo el contexto "/v1/api/financier".
 */
@RestController
@RequestMapping("v1/api/financier")
public class FinancierController {

    private final IFinancierService financierService;

    /**
     * Constructor que inyecta el servicio de financieros requerido por el controlador.
     *
     * @param financierService Servicio de financieros que implementa la lógica de negocio.
     */
    @Autowired
    public FinancierController(IFinancierService financierService) {
        this.financierService = financierService;
    }

    /**
     * Maneja la solicitud GET para obtener todos los financieros.
     *
     * @return ResponseEntity con la lista de financieros y el estado HTTP correspondiente.
     */
    @GetMapping("/getAll")
    public ResponseEntity<List<FinancierResponseDTO>> getAllFinanciers() {
        return ResponseEntity.ok(financierService.getAllFinanciers());
    }

    /**
     * Maneja la solicitud GET para obtener un financiero por su ID.
     *
     * @param id ID del financiero que se desea obtener.
     * @return ResponseEntity con el financiero encontrado y el estado HTTP correspondiente.
     */
    @GetMapping("/{id}")
    public ResponseEntity<FinancierResponseDTO> getFinancierById(@PathVariable UUID id) {
        return ResponseEntity.ok(financierService.getFinancierById(id));
    }
    /**
     * Maneja la solicitud POST para registrar un nuevo financiero.
     *
     * @param financier DTO que contiene la información del financiero a registrar.
     * @return ResponseEntity con el financiero registrado y el estado HTTP correspondiente.
     */

    @PostMapping("/register")
    public ResponseEntity<FinancierResponseDTO> registerFinancier(@RequestBody @Valid FinancierRegisterDTO financier) {
        return ResponseEntity.ok(financierService.registerFinancier(financier));
    }
}
