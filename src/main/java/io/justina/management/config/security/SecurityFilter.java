package io.justina.management.config.security;

import io.justina.management.service.token.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * Filtro de seguridad para validar y establecer la autenticación basada en tokens JWT.
 * Extiende OncePerRequestFilter para garantizar que se ejecute una vez por cada solicitud.
 */
@Component
public class SecurityFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final UserDetailsService userDetailsService;

    /**
     * Constructor que inicializa el filtro de seguridad con los servicios necesarios.
     *
     * @param tokenService           Servicio para operaciones relacionadas con tokens JWT

     */
    @Autowired
    public SecurityFilter(TokenService tokenService, UserDetailsService userDetailsService) {
        this.tokenService = tokenService;
        this.userDetailsService = userDetailsService;

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
    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        System.out.println("authHeader = " + authHeader);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            String username = tokenService.getUsernameFromToken(token);
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                System.out.println("Email autenticado desde security filter = " + userDetails);
                if (tokenService.validateToken(token, userDetails)) {
                    String role = tokenService.getRoleFromToken(token);
                    System.out.println("Rol autenticado desde security filter= " + role);
                    List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role));
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
                    System.out.println("authentication desde security filter= " + authentication);
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    System.out.println("Authentication set in SecurityContext");
                    System.out.println("Request URI: " + request.getRequestURI());
                    System.out.println("Request method: " + request.getMethod());
                }
            }
        }
        filterChain.doFilter(request, response);
    }

}

