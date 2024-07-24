package io.justina.management.dto.financier;



import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Clase DTO (Data Transfer Object) que representa la información de registro de un financiero.
 */
@Data
public class FinancierRegisterDTO {

    /**
     * Nombre del financiero.
     */
    @NotBlank(message = "El nombre es obligatorio")
    private String name;
    /**
     * Plan asociado con el financiero.
     */
    @NotBlank(message = "El plan es obligatorio")
    private String plan;
    /**
     * Información de contacto o datos asociados con el financiero.
     */
    @NotBlank(message = "Los datos de contacto son obligatorios")
    private String dataContact;
}
