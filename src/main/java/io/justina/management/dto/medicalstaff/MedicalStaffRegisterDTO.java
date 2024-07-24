package io.justina.management.dto.medicalstaff;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * Clase DTO (Data Transfer Object) que representa la información de registro de un personal médico.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedicalStaffRegisterDTO {
    /**
     * Nombre del personal médico.
     */
    @NotBlank(message = "El nombre es obligatorio")
    private String firstName;
    /**
     * Apellido del personal médico.
     */
    @NotBlank(message = "El apellido es obligatorio")
    private String lastName;
    /**
     * Correo electrónico del personal médico.
     */
    @NotBlank(message = "El correo electrónico es obligatorio")
    @Email(message = "Debe ser una dirección de correo electrónico válida")
    private String email;
    /**
     * Contraseña del personal médico.
     */
    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String password;
    /**
     * Número de teléfono del personal médico.
     */
    private String phone;
    /**
     * Número de registro médico del personal.
     */
    @NotNull(message = "El número de registro médico es obligatorio")
    private Integer medicalRegistrationNumber;
    /**
     * Especialidades médicas del personal.
     */
    private String specialities;
    /**
     * Descripción del perfil del personal médico.
     */
    private String description;
    /**
     * Indicador de activación del personal médico.
     */
    private Boolean active;
}
