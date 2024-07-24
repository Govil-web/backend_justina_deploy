package io.justina.management.service.user;

import io.justina.management.dto.user.UserRegisterDataDTO;
import io.justina.management.dto.user.UserResponseDataDTO;

import java.util.List;

/**
 * Interfaz que define los métodos para gestionar usuarios en el sistema.
 */
public interface IUserService {

    /**
     * Registra un nuevo usuario en el sistema.
     *
     * @param userRegisterDataDTO DTO con los datos del usuario que se desea registrar.
     * @return DTO que representa al usuario registrado.
     */
    UserResponseDataDTO registerUser(UserRegisterDataDTO userRegisterDataDTO);

    /**
     * Obtiene todos los usuarios registrados en el sistema.
     *
     * @return Lista de todos los usuarios como objetos UserResponseDataDTO.
     */
    List<UserResponseDataDTO> getAllUsers();

    /**
     * Obtiene un usuario por su ID.
     *
     * @param id ID del usuario que se desea obtener.
     * @return DTO que representa al usuario encontrado.
     */
    UserResponseDataDTO getUserById(Long id);
}
