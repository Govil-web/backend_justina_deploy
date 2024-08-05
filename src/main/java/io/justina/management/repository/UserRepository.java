package io.justina.management.repository;

import io.justina.management.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Interfaz de repositorio para la entidad User.
 * Esta interfaz proporciona métodos para realizar operaciones de persistencia y consulta
 * sobre entidades de tipo User en la base de datos.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Busca y devuelve el usuario asociado al correo electrónico especificado.
     *
     * @param email Correo electrónico del usuario a buscar.
     * @return El usuario asociado al correo electrónico especificado.
     */
    User findByEmail(String email);
    /**
     * Verifica si existe un usuario con el correo electrónico especificado.
     *
     * @param email Correo electrónico del usuario a verificar.
     * @return Verdadero si existe un usuario con el correo electrónico especificado, falso en caso contrario.
     */
    boolean existsByEmail(String email);
}
