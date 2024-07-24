package io.justina.management.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
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
public class Patient implements Serializable {

    /**
     * Identificador único del paciente.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_paciente")
    private Long idPatient;

    /**
     * Usuario asociado a este paciente.
     */
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

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
    @Column(name = "grupo_sanguineo")
    private String bloodType;

    /**
     * Factor sanguíneo del paciente.
     */
    @Column(name = "factor_sanguineo")
    private String bloodFactor;

    /**
     * Estado de activo/inactivo del paciente.
     */
    @Column(name = "activo")
    private Boolean active;

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

    /*
     * Posibles relaciones adicionales que pueden estar presentes en el sistema:
     * - Patologías
     * - Personal Médico
     * - Tratamientos
     * - Financiadores
     * - Otros pacientes (relaciones familiares u otros)
     */

}
