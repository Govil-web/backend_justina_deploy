package io.justina.management.config.security;

import io.justina.management.model.MedicalStaff;
import io.justina.management.model.Patient;
import io.justina.management.model.User;
import io.justina.management.repository.MedicalStaffRepository;
import io.justina.management.repository.PatientRepository;
import io.justina.management.repository.UserRepository;
import io.justina.management.service.token.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filtro de seguridad para validar y establecer la autenticación basada en tokens JWT.
 * Extiende OncePerRequestFilter para garantizar que se ejecute una vez por cada solicitud.
 */
@Component
public class SecurityFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final UserRepository userRepository;
    private final MedicalStaffRepository medicalStaffRepository;
    private final PatientRepository patientRepository;

    /**
     * Constructor que inicializa el filtro de seguridad con los servicios necesarios.
     *
     * @param tokenService           Servicio para operaciones relacionadas con tokens JWT
     * @param userRepository         Repositorio de usuarios para la autenticación de roles de administrador
     * @param medicalStaffRepository Repositorio de personal médico para la autenticación de roles de doctor
     */
    @Autowired
    public SecurityFilter(TokenService tokenService, UserRepository userRepository, MedicalStaffRepository medicalStaffRepository,
                          PatientRepository patientRepository) {
        this.tokenService = tokenService;
        this.userRepository = userRepository;
        this.medicalStaffRepository = medicalStaffRepository;
        this.patientRepository = patientRepository;
    }
    /**
     * Implementación del filtro de seguridad para validar y establecer la autenticación basada en tokens JWT.
     *
     * @param request     Objeto HttpServletRequest que representa la solicitud HTTP entrante
     * @param response    Objeto HttpServletResponse que representa la respuesta HTTP saliente
     * @param filterChain Cadena de filtros para continuar el procesamiento de la solicitud
     * @throws ServletException Si hay un error de servlet
     * @throws IOException      Sí hay un error de E/S
     */
    public void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.replace("Bearer ", "");
            String subject = tokenService.getSubject(token);
            if (subject != null) {
                if(tokenService.hasRol(token, "ROLE_ADMIN")){
                    User user = userRepository.findByEmail(subject);
                    System.out.println("This is user: " + user.getUsername() + " with role: " + user.getRoleEnum());
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else if(tokenService.hasRol(token, "ROLE_DOCTOR")){
                    MedicalStaff medicalStaff = medicalStaffRepository.findByEmail(subject);
                    System.out.println("This is medicalStaff: " + medicalStaff.getUsername() + " with role: " + medicalStaff.getRoleEnum());
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(medicalStaff, null, medicalStaff.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else if (tokenService.hasRol(token, "ROLE_PATIENT")){
                    Patient patient = patientRepository.findByEmail(subject);
                    System.out.println("This is patient: " + patient.getUsername()  + " with role: " + patient.getRoleEnum());
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(patient, null, patient.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }

            }
        }
        filterChain.doFilter(request, response);
    }

}

