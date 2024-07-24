package io.justina.management.service.financier;

import io.justina.management.dto.financier.FinancierRegisterDTO;
import io.justina.management.dto.financier.FinancierResponseDTO;


import java.util.List;
import java.util.UUID;

/**
 * Interfaz que define los métodos para gestionar financiadores en el sistema.
 */
public interface IFinancierService {

    /**
     * Obtiene todos los financiadores registrados en el sistema.
     *
     * @return Lista de todos los financiadores como objetos FinancierResponseDTO.
     */
    List<FinancierResponseDTO> getAllFinanciers();

    /**
     * Obtiene un financiador por su ID.
     *
     * @param financierId ID del financiador que se desea obtener.
     * @return DTO que representa al financiador encontrado.
     */
    FinancierResponseDTO getFinancierById(UUID financierId);

    /**
     * Registra un nuevo financiador en el sistema.
     *
     * @param financier DTO con los datos del financiador que se desea registrar.
     * @return DTO que representa al financiador registrado.
     */
    FinancierResponseDTO registerFinancier(FinancierRegisterDTO financier);
}

