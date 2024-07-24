package io.justina.management.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "especialidades")
@Entity
public class Especialidad {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_especialidad")
    private UUID idEspecialidad;

    @Column(name = "tipo_especialidad")
    private String tipoEspecialidad;
}
