package org.victorpiles.escacs.api.game;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * {@link JpaRepository Repositori} relacionat amb les {@link Game partides}.
 *
 * @author Víctor Piles
 * @version 1.0
 */
@Repository
@Transactional(readOnly = true)
public interface GameRepository extends JpaRepository<Game, Long> {
}
