package io.justina.management.controller.user;

import io.justina.management.dto.user.UserRegisterDataDTO;
import io.justina.management.dto.user.UserResponseDataDTO;
import io.justina.management.service.user.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
/**
 * Controlador REST que maneja las operaciones relacionadas con los usuarios.
 *
 * <p>Este controlador maneja las solicitudes REST para operaciones CRUD sobre los usuarios,
 * utilizando la ruta base "/v1/api/user".
 */
@RestController
@RequestMapping("v1/api/user")
public class UserController {


    private final IUserService userService;
    /**
     * Constructor que inyecta el servicio de usuarios requerido por el controlador.
     *
     * @param userService Servicio de usuarios que implementa la lógica de negocio.
     */
    @Autowired
    public UserController(IUserService userService){
        this.userService = userService;
    }

    /**
     * Maneja la solicitud POST para registrar un nuevo usuario.
     *
     * @param userRegisterDataDTO DTO que contiene la información del usuario a registrar.
     * @return ResponseEntity con el usuario registrado y el estado HTTP correspondiente.
     */
    @PostMapping("/register")
    @Operation(summary = "Register a new user")
    public ResponseEntity<UserResponseDataDTO> registerUser(@RequestBody @Valid UserRegisterDataDTO userRegisterDataDTO){
        UserResponseDataDTO user = userService.registerUser(userRegisterDataDTO);
        return ResponseEntity.ok(user);
    }
    /**
     * Maneja la solicitud GET para obtener todos los usuarios.
     *
     * @return ResponseEntity con la lista de usuarios y el estado HTTP correspondiente.
     */
    @GetMapping("/getAll")
    @Operation(summary = "Get all users")
    public ResponseEntity<List<UserResponseDataDTO>> getAllUsers(){
        return ResponseEntity.ok(userService.getAllUsers());
    }
    /**
     * Maneja la solicitud GET para obtener un usuario por su ID.
     *
     * @param id ID del usuario que se desea obtener.
     * @return ResponseEntity con el usuario encontrado y el estado HTTP correspondiente.
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get user by id")
    public ResponseEntity<UserResponseDataDTO> getUserById(@PathVariable Long id){
        return ResponseEntity.ok(userService.getUserById(id));
    }
}
