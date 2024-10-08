package io.justina.management.repository;


import io.justina.management.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Interfaz de repositorio para la entidad Patient.
 * Esta interfaz proporciona métodos para realizar operaciones de persistencia y consulta
 * sobre entidades de tipo Patient en la base de datos.
 */
@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    /**
     * Verifica si existe un paciente con el correo electrónico especificado.
     *
     * @param email Correo electrónico del paciente a verificar.
     * @return Verdadero si existe un paciente con el correo electrónico especificado, falso en caso contrario.
     */
    boolean existsByEmail(String email);

}

