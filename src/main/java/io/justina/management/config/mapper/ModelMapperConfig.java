package io.justina.management.config.mapper;


import io.justina.management.dto.patient.PatientResponseDTO;
import io.justina.management.model.Patient;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * Configuración para la creación de un bean ModelMapper.
 * Define un bean de Spring para utilizar ModelMapper en la aplicación.
 */
@Component
public class ModelMapperConfig {
    /**
     * Crea y configura un bean de ModelMapper para mapeo de objetos.
     *
     * @return Instancia de ModelMapper configurada
     */
    @Bean
    public ModelMapper modelMapperPatient() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.typeMap(Patient.class, PatientResponseDTO.class)
                .addMappings(mapper -> {
                    mapper.map(Patient::getId, PatientResponseDTO::setId);
                    mapper.map(Patient::getFirstName, PatientResponseDTO::setFirstName);
                    mapper.map(Patient::getLastName, PatientResponseDTO::setLastName);
                    mapper.map(Patient::getEmail, PatientResponseDTO::setEmail);
                    mapper.map(Patient::getIdentificationNumber, PatientResponseDTO::setIdentificationNumber);
                    mapper.map(Patient::getBirthDate, PatientResponseDTO::setBirthDate);
                    mapper.map(Patient::getBloodType, PatientResponseDTO::setBloodType);
                    mapper.map(Patient::getBloodFactor, PatientResponseDTO::setBloodFactor);
                    mapper.map(Patient::getActive, PatientResponseDTO::setActive);
                });
        return modelMapper;
    }



}
