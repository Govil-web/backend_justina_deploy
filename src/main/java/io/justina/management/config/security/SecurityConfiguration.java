package io.justina.management.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
/**
 * Configuración de seguridad de la aplicación.
 * Define las reglas de seguridad, el filtro de seguridad y los beans necesarios.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private final SecurityFilter securityFilter;
    /**
     * Constructor que inicializa la configuración de seguridad con un filtro personalizado.
     *
     * @param securityFilter Filtro de seguridad personalizado
     */
    @Autowired
    public SecurityConfiguration(SecurityFilter securityFilter) {
        this.securityFilter = securityFilter;
    }

    /**
     * Configura la cadena de filtros de seguridad HTTP.
     *
     * @param httpSecurity Configuración de seguridad HTTP
     * @return Cadena de filtros de seguridad configurada
     * @throws Exception Sí hay un error al configurar la seguridad
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Rutas públicas
                        .requestMatchers(HttpMethod.GET, "/").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/user/add", "/api/login").permitAll()
                        .requestMatchers("/swagger-ui.html", "/v3/api-docs/**", "/swagger-ui/**").permitAll()

                        // Rutas compartidas entre ADMIN / PATIENT / DOCTOR
                        .requestMatchers(HttpMethod.GET, "/api/patient/{id}", "/api/medical/getActive").hasAnyRole("ADMIN", "PATIENT")
                        .requestMatchers(HttpMethod.GET, "/api/appointment/getByPatient/{id}").hasAnyRole("ADMIN", "PATIENT")
                        .requestMatchers(HttpMethod.POST, "/api/appointment/add").hasAnyRole("PATIENT", "ADMIN")
                        .requestMatchers(HttpMethod.GET,"/api/appointment/getByMedicalStaff/{id}").hasAnyRole("ADMIN", "DOCTOR")
                        .requestMatchers(HttpMethod.GET, "/api/medical/{id}").hasAnyRole("ADMIN", "DOCTOR")
                        .requestMatchers(HttpMethod.GET, "/api/appointment/getByMedicalStaff/{id}").hasAnyRole("ADMIN", "DOCTOR")


                        // Rutas específicas para ADMIN
                        .requestMatchers(HttpMethod.GET,
                                "/api/user", "/api/user/**",
                                "/api/medical",
                                "/api/financier", "/api/financier/**",
                                "/api/appointment", "/api/patient").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST,
                                "/api/medical/add",
                                "/api/financier/add", "/api/patient/add").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE,
                                "/api/medical/delete/{id}",
                                "/api/appointment/delete/{id}").hasRole("ADMIN")

                        // Acceso total para ADMIN a cualquier otra solicitud
                        .anyRequest().hasRole("ADMIN"))
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    /**
     * Configura el administrador de autenticación para la aplicación.
     *
     * @param authenticationConfiguration Configuración de autenticación
     * @return Administrador de autenticación configurado
     * @throws Exception Sí hay un error al obtener el administrador de autenticación
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * Configura el codificador de contraseñas para la aplicación.
     *
     * @return Codificador de contraseñas BCrypt
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
