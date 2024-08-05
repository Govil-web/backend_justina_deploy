package io.justina.management.model;

import io.justina.management.enums.RoleEnum;
import io.justina.management.enums.Specialty;
import io.justina.management.utils.interfaces.Identifiable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;


/**
 * Clase que representa al personal médico.
 * Esta clase contiene información sobre el personal médico, incluyendo su identificación,
 * usuario asociado, número de teléfono, número de registro médico, especialidad,
 * descripción y citas programadas.
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "medical_staff")
public class MedicalStaff implements UserDetails,Identifiable {

    /**
     * Identificador único del personal médico.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_personal_medico")
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
     * Estado de activo/inactivo del usuario.
     */
    @Column(name = "activo")
    private Boolean active;


    /**
     * Número de teléfono del personal médico.
     */
    @Column(name="teléfono")
    private String phone;

    /**
     * Número de registro médico del personal médico.
     */
    @Column(name = "numero_registro_medico")
    private Integer medicalRegistrationNumber;

    /**
     * Especialidad(es) del personal médico.
     */
    @Column(name = "especialidad")
    @Enumerated(EnumType.STRING)
    private Specialty specialities;

    /**
     * Descripción del personal médico.
     */
    @Column(name = "descripción")
    private String description;


    @Override
    public Long getPrimaryKey() {
        return this.id;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_DOCTOR"));
    }
    /**
     * Método para obtener la contraseña del usuario.
     */
    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
