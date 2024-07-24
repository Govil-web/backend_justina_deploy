package io.justina.management.service.user;

import io.justina.management.dto.user.UserRegisterDataDTO;
import io.justina.management.dto.user.UserResponseDataDTO;
import io.justina.management.enums.RoleEnum;
import io.justina.management.model.User;
import io.justina.management.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementación del servicio para gestionar usuarios en el sistema.
 */
@Service
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper = new ModelMapper();

    /**
     * Constructor de la clase UserService.
     *
     * @param userRepository Repositorio de usuarios utilizado para acceder a la capa de persistencia.
     * @param passwordEncoder Encoder utilizado para codificar las contraseñas de usuario.
     */
    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = (BCryptPasswordEncoder) passwordEncoder;
    }

    /**
     * Registra un nuevo usuario en el sistema.
     *
     * @param userRegisterDataDTO DTO con los datos del usuario que se desea registrar.
     * @return DTO que representa al usuario registrado.
     */
    @Override
    @Transactional
    public UserResponseDataDTO registerUser(UserRegisterDataDTO userRegisterDataDTO) {
        if(userRepository.existsByEmail(userRegisterDataDTO.getEmail())) {
            throw new RuntimeException("User already exists with email: " + userRegisterDataDTO.getEmail());
        }
        User user = modelMapper.map(userRegisterDataDTO, User.class);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setActive(true);
        user.setRoleEnum(RoleEnum.valueOf(user.getRoleEnum().name()));
        userRepository.save(user);
        return modelMapper.map(user, UserResponseDataDTO.class);
    }

    /**
     * Obtiene todos los usuarios registrados en el sistema.
     *
     * @return Lista de todos los usuarios con rol de administrador como objetos UserResponseDataDTO.
     * @throws RuntimeException Si el usuario autenticado no tiene el rol de administrador.
     */
    @Override
    public List<UserResponseDataDTO> getAllUsers() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean hasAdminRole = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
        if (!hasAdminRole) {
            throw new RuntimeException("Access denied. Only admins can perform this action.");
        }
        List<User> users = userRepository.findAll();
        List<User> adminUsers = users.stream()
                .filter(user -> user.getRoleEnum() == RoleEnum.ROLE_ADMIN)
                .collect(Collectors.toList());
        Type listType = new org.modelmapper.TypeToken<List<UserResponseDataDTO>>() {}.getType();
        return modelMapper.map(adminUsers, listType);
    }

    /**
     * Obtiene un usuario por su ID.
     *
     * @param id ID del usuario que se desea obtener.
     * @return DTO que representa al usuario encontrado.
     * @throws RuntimeException Si no se encuentra un usuario con el ID especificado o si el usuario encontrado no tiene el rol de administrador.
     */
    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public UserResponseDataDTO getUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent() && user.get().getRoleEnum() == RoleEnum.ROLE_ADMIN) {
            return modelMapper.map(user.get(), UserResponseDataDTO.class);
        } else {
            throw new RuntimeException("User not found with id: " + id);
        }
    }
}
