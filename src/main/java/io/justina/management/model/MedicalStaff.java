package io.justina.management.model;

import io.justina.management.enums.Specialty;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


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
@PrimaryKeyJoinColumn(name = "user_id")
@DiscriminatorValue("MEDICAL_STAFF")
public class MedicalStaff extends User{

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


}
