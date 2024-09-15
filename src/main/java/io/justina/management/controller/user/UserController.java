package io.justina.management.controller.user;

import io.justina.management.dto.apiresponse.ApiResponse;
import io.justina.management.dto.user.UserRegisterDataDTO;
import io.justina.management.dto.user.UserResponseDataDTO;
import io.justina.management.exception.BadRequestException;
import io.justina.management.service.user.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST que maneja las operaciones relacionadas con los usuarios.
 *
 * <p>Este controlador maneja las solicitudes REST para operaciones CRUD sobre los usuarios,
 * utilizando la ruta base "/v1/api/user".
 */
@RestController
@RequestMapping("api/user")
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
    @PostMapping("/add")
    @Operation(summary = "Register a new user")
    public ResponseEntity<ApiResponse<UserResponseDataDTO>> registerUser(@RequestBody @Valid UserRegisterDataDTO userRegisterDataDTO){
        UserResponseDataDTO user = userService.registerUser(userRegisterDataDTO);
        if(user == null){
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, "Usuario no registrado", null));
        }
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Usuario registrado", user));
    }
    /**
     * Maneja la solicitud GET para obtener todos los usuarios.
     *
     * @return ResponseEntity con la lista de usuarios y el estado HTTP correspondiente.
     */
    @GetMapping
    @Operation(summary = "Get all users")
    public ResponseEntity<ApiResponse<UserResponseDataDTO>> getAllUsers(){
        try{
            Iterable<UserResponseDataDTO> users = userService.getAllUsers();
            return ResponseEntity.ok(new ApiResponse<>(true, "Usuarios encontrados", users));
        }catch (BadRequestException e){
            throw new BadRequestException("Ha ocurrido un error "+ e.getMessage());
        }
    }
    /**
     * Maneja la solicitud GET para obtener un usuario por su ID.
     *
     * @param id ID del usuario que se desea obtener.
     * @return ResponseEntity con el usuario encontrado y el estado HTTP correspondiente.
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get user by id")
    public ResponseEntity <ApiResponse<UserResponseDataDTO>> getUserById(@PathVariable Long id){
        UserResponseDataDTO user = userService.getUserById(id);
        if(user != null){
            return new ResponseEntity<>(new ApiResponse<>(true, "Usuario encontrado", user), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(new ApiResponse<>(false, "Usuario no encontrado", null), HttpStatus.NOT_FOUND);
        }
    }
}
