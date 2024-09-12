package io.justina.management.repository;

import io.justina.management.model.MedicalStaff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Interfaz de repositorio para la entidad MedicalStaff.
 * Esta interfaz proporciona métodos para realizar operaciones de persistencia y consulta
 * sobre entidades de tipo MedicalStaff en la base de datos.
 */
@Repository
public interface MedicalStaffRepository extends JpaRepository<MedicalStaff, Long> {
    List<MedicalStaff> findByActiveTrue();
}

