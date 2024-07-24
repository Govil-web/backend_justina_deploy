package io.justina.management.controller.authentication;


import io.justina.management.dto.jwttoken.DataJWTTokenDTO;
import io.justina.management.dto.user.UserAuthenticateDataDTO;
import io.justina.management.service.authentication.IAuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador REST para autenticación de usuarios.
 * Maneja las operaciones relacionadas con la autenticación de usuarios.
 */
@RestController
@RequestMapping("v1/api/login")
public class AuthenticationController {

    private final IAuthenticationService authenticationService;

    /**
     * Constructor para la clase AuthenticationController.
     *
     * @param authenticationService Servicio de autenticación que se inyecta mediante Spring
     */
    @Autowired
    public AuthenticationController(IAuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    /**
     * Endpoint para autenticar un usuario.
     *
     * @param userAuthenticateDataDTO Datos del usuario para autenticación
     * @return ResponseEntity con el token JWT generado
     */
    @Operation(summary = "Authenticate user")
    @PostMapping
    public ResponseEntity<DataJWTTokenDTO> authenticate(@RequestBody @Valid UserAuthenticateDataDTO userAuthenticateDataDTO){
        DataJWTTokenDTO dataJWTTokenDTO = authenticationService.authenticate(userAuthenticateDataDTO);
        return ResponseEntity.ok(dataJWTTokenDTO);
    }
}

