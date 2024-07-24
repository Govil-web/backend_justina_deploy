package io.justina.management.dto.financier;


import lombok.Data;
import java.util.UUID;
/**
 * Clase DTO (Data Transfer Object) que representa la respuesta de información de un financiero.
 */
@Data
public class FinancierResponseDTO {

    /**
     * Identificador único del financiero.
     */
    private UUID idFinancier;
    /**
     * Nombre del financiero.
     */
    private String name;
    /**
     * Plan asociado con el financiero.
     */
    private String plan;
    /**
     * Información de contacto o datos asociados con el financiero.
     */
    private String dataContact;
}
