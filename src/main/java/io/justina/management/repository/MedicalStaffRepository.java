package io.justina.management.repository;

import io.justina.management.model.MedicalStaff;
import io.justina.management.model.User;
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

    /**
     * Busca y devuelve el personal médico asociado al usuario con el correo electrónico especificado.
     *
     * @param email Correo electrónico del usuario asociado al personal médico.
     * @return El personal médico asociado al usuario con el correo electrónico especificado.
     */
    MedicalStaff findByUser_Email(String email);

    /**
     * Busca y devuelve una lista de todo el personal médico que esté activo.
     *
     * @return Lista de personal médico activo.
     */
    List<MedicalStaff> findByUser_ActiveTrue();

    /**
     * Busca y devuelve el personal médico asociado al usuario especificado.
     *
     * @param user Usuario asociado al personal médico.
     * @return El personal médico asociado al usuario especificado.
     */
    MedicalStaff findByUser(User user);


}

