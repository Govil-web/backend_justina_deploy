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
                        .requestMatchers(HttpMethod.POST, "v1/api/user/register", "v1/api/login").permitAll()
                        .requestMatchers("/swagger-ui.html", "/v3/api-docs/**", "/swagger-ui/**").permitAll()

                        // Rutas para ADMIN
                        .requestMatchers(HttpMethod.GET,
                                "v1/api/user/getAll", "v1/api/user/**",
                                "v1/api/medical-staff/getAll", "v1/api/medical-staff/getActive",
                                "v1/api/financier/getAll", "v1/api/financier/**",
                                "v1/api/appointment/getAll", "v1/api/appointment/getByPatient/{id}",
                                "v1/api/appointment/getByMedicalStaff/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST,
                                "v1/api/appointment/register",
                                "v1/api/medical-staff/register",
                                "v1/api/financier/").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE,
                                "v1/api/medical-staff/delete/{id}",
                                "v1/api/appointment/delete/{id}").hasRole("ADMIN")

                        // Rutas para PATIENT
                        .requestMatchers(HttpMethod.GET,
                                "v1/api/appointment/getByPatient/{id}","v1/api/medical-staff/getActive",
                                "v1/api/patient/{id}").hasAnyRole("ADMIN", "PATIENT")
                        .requestMatchers(HttpMethod.POST, "v1/api/patient/register",
                                "v1/api/appointment/register").hasAnyRole("ADMIN", "PATIENT")

                        // Rutas para DOCTOR
                        .requestMatchers(HttpMethod.GET, "v1/api/medical-staff/{id}").hasAnyRole("ADMIN", "DOCTOR")
                        .requestMatchers(HttpMethod.GET, "v1/api/appointment/getByMedicalStaff/{id}").hasAnyRole("ADMIN", "DOCTOR")

                        // Cualquier otra solicitud requiere autenticación
                        .anyRequest().authenticated())
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
