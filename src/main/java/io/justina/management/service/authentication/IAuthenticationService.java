package io.justina.management.service.authentication;

import io.justina.management.dto.jwttoken.DataJWTTokenDTO;
import io.justina.management.dto.user.UserAuthenticateDataDTO;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Interfaz que define métodos para la autenticación y gestión de usuarios en el sistema.
 */
public interface IAuthenticationService {

    /**
     * Carga los detalles del usuario por su nombre de usuario (correo electrónico).
     *
     * @param username Nombre de usuario (correo electrónico) del usuario.
     * @return Detalles del usuario encontrado.
     * @throws UsernameNotFoundException Si el usuario no se encuentra en la base de datos.
     */
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

    /**
     * Autentica a un usuario con las credenciales proporcionadas y devuelve un token JWT.
     *
     * @param userAuthenticateDataDTO Datos de autenticación del usuario (correo electrónico y contraseña).
     * @return DTO que contiene el token JWT generado.
     */
    DataJWTTokenDTO authenticate(UserAuthenticateDataDTO userAuthenticateDataDTO);

    /**
     * Verifica si el usuario autenticado tiene el rol de administrador.
     *
     * @param authentication Objeto de autenticación del usuario.
     * @return true si el usuario es administrador, false de lo contrario.
     */
    boolean isAdmin(Authentication authentication);

    /**
     * Verifica si el usuario autenticado es el propietario del usuario con el ID especificado.
     *
     * @param id ID del usuario que se desea verificar si es el propietario.
     * @return true si el usuario autenticado es el propietario del usuario con el ID especificado, false de lo contrario.
     */
    boolean isAuthenticatedUserOwner(Long id);

    /**
     * Verifica si el usuario autenticado tiene acceso al recurso con el ID especificado.
     *
     * @param id ID del recurso que se desea verificar si el usuario tiene acceso.
     */
    void verifyUserAccess(Long id);

    /**
     * Obtiene el ID del usuario autenticado.
     *
     * @return ID del usuario autenticado.
     */
    Long getAuthenticatedUserId();
}
