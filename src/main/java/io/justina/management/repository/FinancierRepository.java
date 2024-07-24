package io.justina.management.repository;

import io.justina.management.model.Financier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Interfaz de repositorio para la entidad Financier.
 * Esta interfaz proporciona métodos para realizar operaciones de persistencia y consulta
 * sobre entidades de tipo Financier en la base de datos.
 */
@Repository
public interface FinancierRepository extends JpaRepository<Financier, UUID> {

}

