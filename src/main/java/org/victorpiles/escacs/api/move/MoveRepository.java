package org.victorpiles.escacs.api.move;

import org.springframework.data.jpa.repository.JpaRepository;
import org.victorpiles.escacs.api.user.User;

import java.util.List;

/**
 * {@link JpaRepository Repositori} relacionat amb els {@link Move moviments}.
 *
 * @author Víctor Piles
 * @version 1.0
 */
public interface MoveRepository extends JpaRepository<Move, Long> {

    /**
     * Busca en la base de dades els {@link Move moviments} executats per l'{@link User usuari} passat com a paràmetre.
     *
     * @param user L'{@link User usuari} a buscar.
     *
     * @return El {@link List llistat} de {@link Move moviments} realitzats per l'{@link User usuari}.
     */
    List<Move> findAllByUser(User user);
}