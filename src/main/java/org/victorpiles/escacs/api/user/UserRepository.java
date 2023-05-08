package org.victorpiles.escacs.api.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * {@link JpaRepository Repositori} relacionat amb els {@link User usuaris}.
 *
 * @author Víctor Piles
 * @version 1.0
 */
@Repository
@Transactional(readOnly = true)
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Busca en la base de dades l'{@link User usuari} amb l'{@link User#getEmail() email} passat com a paràmetre.
     *
     * @param email L'email a buscar.
     *
     * @return L'{@link User usuari} amb l'{@link User#getEmail() email} passat com a paràmetre, si aquest existeix.
     */
    Optional<User> findByEmail(String email);

    /**
     * Busca en la base de dades l'{@link User usuari} amb el {@link User#getUsername() nom} passat com a paràmetre.
     *
     * @param username L'usuari a buscar.
     *
     * @return L'{@link User usuari} amb el {@link User#getUsername() nom} passat com a paràmetre, si aquest existeix.
     */
    Optional<User> findByUsername(String username);
}