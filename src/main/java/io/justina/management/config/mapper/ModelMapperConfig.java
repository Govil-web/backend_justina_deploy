package io.justina.management.config.mapper;

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
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
