package io.justina.management.model;



import io.justina.management.enums.RoleEnum;
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
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;


/**
 * Clase que representa a un paciente en el sistema.
 * Esta clase contiene información personal y médica del paciente, incluyendo su identificación,
 * datos de usuario asociado, número de documento, fecha de nacimiento, grupo sanguíneo, factor sanguíneo,
 * estado de activo/inactivo, sexo, citas programadas y otras posibles relaciones médicas.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "pacientes")
@Entity
public class Patient implements  Identifiable, UserDetails {

    /**
     * Identificador único del paciente.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_paciente")
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
     * Número de documento del paciente.
     */
    @Column(name = "numero_documento")
    private String identificationNumber;

    /**
     * Fecha de nacimiento del paciente.
     */
    @Column(name = "fecha_nacimiento")
    private LocalDate birthDate;

    /**
     * Grupo sanguíneo del paciente.
     */
    @Column(name = "grupo_sanguíneo")
    private String bloodType;

    /**
     * Factor sanguíneo del paciente.
     */
    @Column(name = "factor_sanguíneo")
    private String bloodFactor;

    /**
     * Sexo del paciente (M, F, etc.).
     */
    @Column(name = "sexo")
    private char sex;

    /**
     * Lista de citas programadas para este paciente.
     */
    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Appointment> appointments;

    @Override
    public Long getPrimaryKey() {
        return this.id;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_PATIENT"));
    }

    @Override
    public String getUsername() {
        return email;
    }
    /**
     * Método para obtener la contraseña del usuario.
     */
    @Override
    public String getPassword() {
        return password;
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

    /*
     * Posibles relaciones adicionales que pueden estar presentes en el sistema:
     * - Patologías
     * - Personal Médico
     * - Tratamientos
     * - Financiadores
     * - Otros pacientes (relaciones familiares u otros)
     */

}
