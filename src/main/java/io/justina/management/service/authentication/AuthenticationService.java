package io.justina.management.service.authentication;

import io.justina.management.dto.jwttoken.DataJWTTokenDTO;
import io.justina.management.dto.user.UserAuthenticateDataDTO;
import io.justina.management.exception.BadRequestException;
import io.justina.management.model.MedicalStaff;
import io.justina.management.model.Patient;
import io.justina.management.model.User;
import io.justina.management.repository.MedicalStaffRepository;
import io.justina.management.repository.PatientRepository;
import io.justina.management.repository.UserRepository;
import io.justina.management.service.token.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Servicio de autenticación que implementa la interfaz UserDetailsService y IAuthenticationService.
 * Este servicio gestiona la autenticación de usuarios, la generación de tokens JWT,
 * y la verificación de roles y permisos de acceso.
 */
@Service
public class AuthenticationService implements UserDetailsService, IAuthenticationService {

    private final UserRepository userRepository;
    private final MedicalStaffRepository medicalStaffRepository;
    private final PatientRepository patientRepository;
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;

    /**
     * Constructor para inicializar el servicio de autenticación.
     *
     * @param userRepository       Repositorio de usuarios para buscar usuarios por correo electrónico.
     * @param tokenService         Servicio para la generación de tokens JWT.
     * @param authenticationManager Administrador de autenticación para autenticar usuarios.
     */
    @Autowired
    public AuthenticationService(UserRepository userRepository, TokenService tokenService, @Lazy AuthenticationManager authenticationManager,
                                 MedicalStaffRepository medicalStaffRepository, PatientRepository patientRepository) {
        this.userRepository = userRepository;
        this.tokenService = tokenService;
        this.authenticationManager = authenticationManager;
        this.medicalStaffRepository = medicalStaffRepository;
        this.patientRepository = patientRepository;

    }

    /**
     * Carga los detalles del usuario por su nombre de usuario (correo electrónico).
     *
     * @param username Nombre de usuario (correo electrónico) del usuario.
     * @return Detalles del usuario encontrado.
     * @throws UsernameNotFoundException Si el usuario no se encuentra en la base de datos.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails userDetails = userRepository.findByEmail(username);
        if (userDetails == null){
            userDetails = patientRepository.findByEmail(username);
        }
        if(userDetails == null){
            userDetails = medicalStaffRepository.findByEmail(username);
        }
        if(userDetails == null){
            throw new UsernameNotFoundException("User not found");
        }
        return userDetails;
    }
    /**
     * Autentíca a un usuario con las credenciales proporcionadas y genera un token JWT.
     *
     * @param userAuthenticateDataDTO Datos de autenticación del usuario (correo electrónico y contraseña).
     * @return DTO que contiene el token JWT generado.
     */
    @Override
    public DataJWTTokenDTO authenticate(UserAuthenticateDataDTO userAuthenticateDataDTO){
        Authentication authenticationToken = new UsernamePasswordAuthenticationToken(userAuthenticateDataDTO.getEmail(), userAuthenticateDataDTO.getPassword());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        var entityAuth = authentication.getPrincipal();
        String jwtToken;
        if (entityAuth instanceof User) {
            jwtToken = tokenService.generateToken(entityAuth);
        } else if (entityAuth instanceof MedicalStaff) {
           jwtToken = tokenService.generateToken(entityAuth);
        } else if (entityAuth instanceof Patient) {
            jwtToken = tokenService.generateToken(entityAuth);
        } else {
            throw new BadRequestException("Entidad no encontrada");
        }
        return new DataJWTTokenDTO(jwtToken);
    }
//    @Override
//    public DataJWTTokenDTO authenticate(UserAuthenticateDataDTO userAuthenticateDataDTO){
//        Authentication authenticationToken = new UsernamePasswordAuthenticationToken(userAuthenticateDataDTO.getEmail(), userAuthenticateDataDTO.getPassword());
//        var entityAuth = authenticationManager.authenticate(authenticationToken).getPrincipal();
//        var jwtToken = tokenService.generateToken(entityAuth);
//            return new DataJWTTokenDTO(jwtToken);
//    }

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
        Long authenticatedUserId = getAuthenticatedUserId();
        return authenticatedUserId.equals(id);
    }

    /**
     * Verifica si el usuario autenticado tiene acceso al recurso con el ID especificado.
     *
     * @param id ID del recurso que se desea verificar si el usuario tiene acceso.
     * @throws AccessDeniedException Si el usuario no tiene permisos para acceder al recurso.
     */
    @Override
    public void verifyUserAccess(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!isAdmin(authentication) && !isAuthenticatedUserOwner(id)) {
            throw new AccessDeniedException("You do not have permissions to access this resource.");
        }
    }
    /**
     * Obtiene el ID del usuario autenticado.
     *
     * @return ID del usuario autenticado.
     * @throws IllegalStateException Si no se puede obtener el ID del usuario autenticado.
     */
    @Override
    public Long getAuthenticatedUserId() {
        Long id;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof MedicalStaff medicalStaff) {
            id = medicalStaff.getId();
        } else if (authentication.getPrincipal() instanceof Patient patient) {
            id = patient.getId();
        }else{
            throw new IllegalStateException("Could not get authenticated user ID");
        }
        return id;
    }

}