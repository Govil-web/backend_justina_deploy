package io.justina.management.model;


import io.justina.management.enums.Specialty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
public class MedicalStaff {

    /**
     * Identificador único del personal médico.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_personal_medico")
    private Long medicalStaffId;

    /**
     * Usuario asociado a este personal médico.
     */
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    /**
     * Número de teléfono del personal médico.
     */
    @Column(name="telefono")
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
    @Column(name = "descripcion")
    private String description;

    /**
     * Lista de citas programadas para este personal médico.
     */
    @OneToMany(mappedBy = "professional")
    private List<Appointment> appointments;

}
