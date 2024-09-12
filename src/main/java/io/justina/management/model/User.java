package io.justina.management.model;

import io.justina.management.enums.RoleEnum;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;


/**
 * Clase que representa un usuario en el sistema.
 * Esta clase contiene información básica de usuario como nombre, apellido, rol,
 * correo electrónico, contraseña y estado de activo/inactivo.
 */
@Getter
@Setter
@Table(name = "usuarios")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "user_type")
public class User implements UserDetails {

    /**
     * Identificador único del usuario.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nombre del usuario.
     */
    @Column(name = "nombre")
    private String firstName;

    /**
     * Apellido del usuario.
     */
    @Column(name = "apellido")
    private String lastName;

    /**
     * Rol del usuario.
     */
    @Enumerated(EnumType.STRING)
    private RoleEnum roleEnum;

    /**
     * Correo electrónico del usuario (también utilizado como nombre de usuario).
     */
    @Column(name = "email", unique = true)
    private String email;

    /**
     * Contraseña del usuario.
     */
    @Column(name = "password")
    private String password;
    /**
     * Número de teléfono del usuario.
     */
    @Column(name="teléfono")
    private String phone;

    /**
     * Estado de activo/inactivo del usuario.
     */
    @Column(name = "activo")
    private Boolean active;

    /**
     * Método para obtener los roles/autoridades del usuario.
     * En este caso, devuelve un único rol basado en el enum `RoleEnum`.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.roleEnum.name()));
    }

    /**
     * Método para obtener la contraseña del usuario.
     */
    @Override
    public String getPassword() {
        return this.password;
    }

    /**
     * Método para obtener el nombre de usuario del usuario (en este caso, el correo electrónico).
     */
    @Override
    public String getUsername() {
        return this.email;
    }

    /**
     * Método para verificar si la cuenta del usuario no ha expirado.
     * En este caso, siempre devuelve true.
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Método para verificar si la cuenta del usuario no está bloqueada.
     * En este caso, siempre devuelve true.
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Método para verificar si las credenciales del usuario no han expirado.
     * En este caso, siempre devuelve true.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Método para verificar si el usuario está habilitado.
     * En este caso, siempre devuelve true.
     */
    @Override
    public boolean isEnabled() {
        return true;
    }

}
