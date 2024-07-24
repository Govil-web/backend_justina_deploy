package io.justina.management.service.financier;

import io.justina.management.dto.financier.FinancierRegisterDTO;
import io.justina.management.dto.financier.FinancierResponseDTO;
import io.justina.management.model.Financier;
import io.justina.management.repository.FinancierRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Implementación del servicio para gestionar financiadores en el sistema.
 */
@Service
public class FinancierServiceImpl implements IFinancierService {

    private final FinancierRepository financierRepository;
    private final ModelMapper modelMapper = new ModelMapper();

    /**
     * Constructor para inicializar el servicio de gestion de financiadores.
     *
     * @param financierRepository Repositorio de financiadores utilizado para acceder a la capa de persistencia.
     */
    @Autowired
    public FinancierServiceImpl(FinancierRepository financierRepository) {
        this.financierRepository = financierRepository;
    }

    /**
     * Obtiene todos los financiadores registrados en el sistema.
     *
     * @return Lista de todos los financiadores como objetos FinancierResponseDTO.
     */
    @Override
    public List<FinancierResponseDTO> getAllFinanciers() {
        List<Financier> financierList = financierRepository.findAll();
        Type listType = new TypeToken<List<FinancierResponseDTO>>() {}.getType();
        return modelMapper.map(financierList, listType);
    }

    /**
     * Obtiene un financiador por su ID.
     *
     * @param financierId ID del financiador que se desea obtener.
     * @return DTO que representa al financiador encontrado.
     * @throws RuntimeException Si el financiador no se encuentra en la base de datos.
     */
    @Override
    public FinancierResponseDTO getFinancierById(UUID financierId) {
        Optional<Financier> financierOptional = financierRepository.findById(financierId);
        if (financierOptional.isPresent()) {
            Financier financier = financierOptional.get();
            return modelMapper.map(financier, FinancierResponseDTO.class);
        } else {
            throw new RuntimeException("Financier not found");
        }
    }

    /**
     * Registra un nuevo financiador en el sistema.
     *
     * @param financier DTO con los datos del financiador que se desea registrar.
     * @return DTO que representa al financiador registrado.
     */
    @Override
    public FinancierResponseDTO registerFinancier(FinancierRegisterDTO financier) {
        Financier financierEntity = modelMapper.map(financier, Financier.class);
        financierRepository.save(financierEntity);
        return modelMapper.map(financierEntity, FinancierResponseDTO.class);
    }
}
