package io.justina.management.service.authentication;

import io.justina.management.dto.jwttoken.DataJWTTokenDTO;
import io.justina.management.dto.user.UserAuthenticateDataDTO;
import io.justina.management.model.MedicalStaff;
import io.justina.management.model.Patient;
import io.justina.management.model.User;
import io.justina.management.service.token.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

/**
 * Servicio de autenticación que implementa la interfaz UserDetailsService y IAuthenticationService.
 * Este servicio gestiona la autenticación de usuarios, la generación de tokens JWT,
 * y la verificación de roles y permisos de acceso.
 */
@Service
public class AuthenticationService implements IAuthenticationService {

    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;

    /**
     * Constructor para inicializar el servicio de autenticación.
     * @param tokenService         Servicio para la generación de tokens JWT.
     * @param authenticationManager Administrador de autenticación para autenticar usuarios.
     */
    @Autowired
    public AuthenticationService(TokenService tokenService, @Lazy AuthenticationManager authenticationManager) {
        this.tokenService = tokenService;
        this.authenticationManager = authenticationManager;

    }

    /**
     * Autentíca a un usuario con las credenciales proporcionadas y genera un token JWT.
     *
     * @param userAuthenticateDataDTO Datos de autenticación del usuario (correo electrónico y contraseña).
     * @return DTO que contiene el token JWT generado.
     */
    @Override
    public DataJWTTokenDTO authenticate(UserAuthenticateDataDTO userAuthenticateDataDTO) {
        Authentication authenticationToken = new UsernamePasswordAuthenticationToken(userAuthenticateDataDTO.getEmail(), userAuthenticateDataDTO.getPassword());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        User user = (User) authentication.getPrincipal();
        String jwtToken = tokenService.generateToken(user);
        System.out.println("Generated token: " + jwtToken + " for user: " + userAuthenticateDataDTO.getEmail());
        return new DataJWTTokenDTO(jwtToken);
    }
    /**
     * Verifica si el usuario autenticado tiene el rol de administrador.
     *
     * @param authentication Objeto de autenticación del usuario.
     * @return true si el usuario es administrador, false de lo contrario.
     */
    @Override
    public boolean isAdmin(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
    }

    /**
     * Verifica si el usuario autenticado es el propietario del usuario con el ID especificado.
     *
     * @param id ID del usuario que se desea verificar si es el propietario.
     * @return true si el usuario autenticado es el propietario del usuario con el ID especificado, false de lo contrario.
     */
    @Override
    public boolean isAuthenticatedUserOwner(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof Patient) {
            Patient patient = (Patient) authentication.getPrincipal();
            System.out.println("isAuthenticatedUserOwner: Authenticated patient ID = " + patient.getId());
            System.out.println("isAuthenticatedUserOwner: Requested patient ID = " + id);
            return patient.getId().equals(id);
        }
        return false;
    }

    /**
     * Verifica si el usuario autenticado tiene acceso al recurso con el ID especificado.
     *
     * @param id ID del recurso que se desea verificar si el usuario tiene acceso.
     * @throws AccessDeniedException Si el usuario no tiene permisos para acceder al recurso.
     */
    @Override
    public boolean verifyUserAccess(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("verifyUserAccess: User authorities = " + authentication.getAuthorities());
        System.out.println("verifyUserAccess: Requested patient ID = " + id);

        if (isAdmin(authentication)) {
            System.out.println("verifyUserAccess: User is ADMIN, access granted");
            return true;
        }

        if (authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_PATIENT"))) {
            boolean isOwner = isAuthenticatedUserOwner(id);
            System.out.println("verifyUserAccess: User is PATIENT, isOwner = " + isOwner);
            return isOwner;
        }

        System.out.println("verifyUserAccess: Access denied");
        return false;
    }
    /**
     * Obtiene el ID del usuario autenticado.
     *
     * @return ID del usuario autenticado.
     * @throws IllegalStateException Si no se puede obtener el ID del usuario autenticado.
     */
    @Override
    public Long getAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof User user) {
            System.out.println("authentication desde el service= " + authentication);

            return user.getId();
        } else {
            throw new IllegalStateException("Could not get authenticated user ID");
        }
    }

}