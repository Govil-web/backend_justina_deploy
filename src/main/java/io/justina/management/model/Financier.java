package io.justina.management.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

/**
 * Clase que representa un financiador.
 * Esta clase está mapeada a la tabla "financiadores" en la base de datos.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "financiadores")
@Entity
public class Financier {

    /**
     * Identificador único del financiador.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_financiador")
    private UUID idFinancier;

    /**
     * Nombre del financiador.
     */
    @Column(name = "nombre")
    private String name;

    /**
     * Plan financiero ofrecido por el financiador.
     */
    @Column(name = "plan")
    private String plan;

    /**
     * Datos de contacto del financiador.
     */
    @Column(name = "datos_contacto")
    private String dataContact;


}


