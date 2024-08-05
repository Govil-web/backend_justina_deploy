package io.justina.management.repository;


import io.justina.management.model.Patient;
import io.justina.management.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Interfaz de repositorio para la entidad Patient.
 * Esta interfaz proporciona métodos para realizar operaciones de persistencia y consulta
 * sobre entidades de tipo Patient en la base de datos.
 */
@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    /**
     * Busca y devuelve el paciente asociado al usuario especificado.
     *
     * @param user Usuario asociado al paciente.
     * @return El paciente asociado al usuario especificado.
     */
    Patient findByUser(User user);

    /**
     * Busca y devuelve el paciente asociado al usuario con el correo electrónico especificado.
     *
     * @param email Correo electrónico del usuario asociado al paciente
     * @return El paciente asociado al usuario con el correo electrónico especificado.
     */
    Patient findByUser_Email(String email);
    Patient findByUser_Id (Long id);
    @Query("SELECT p FROM Patient p WHERE p.id = :id")
    Patient findByPatient_Id(@Param("id") Long id);
}

