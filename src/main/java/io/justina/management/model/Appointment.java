package io.justina.management.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Clase que representa un turno médico.
 * Esta entidad está mapeada a la tabla "turnos" en la base de datos.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "turnos")
@Entity
public class Appointment {

    /**
     * Identificador único del turno.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_turno")
    private Long id;
    /**
     * Centro de atención del turno.
     */
    @Column(name = "centro_atencion")
    private String healthCenter;
    /**
     * Fecha y hora del turno.
     */
    @Column(name = "fecha_turno")
    private LocalDateTime date;
    /**
     * Paciente asignado al turno.
     */
    @ManyToOne//(fetch = FetchType.LAZY)
    @JoinColumn(name = "paciente_id")
    private Patient patient;
    /**
     * Personal médico asignado al turno.
     */
    @ManyToOne//(fetch = FetchType.LAZY)
    @JoinColumn(name = "professional_id")
    private MedicalStaff medicalStaff;





    /*
    * AGREGAR DESCRIPCION PARA EL TURNO
    *private AppointmentType appointmentType;
    private Integer durationMinutes;
    private String notes;
    private String reason;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime cancelledAt;
    private String cancellationReason;
    private boolean reminderSent;
    private boolean confirmed;
    private String location;
    private String virtualMeetingLink;
    private String insuranceDetails;
    private boolean followUpRequired;
    private boolean completed;
    * */
}
