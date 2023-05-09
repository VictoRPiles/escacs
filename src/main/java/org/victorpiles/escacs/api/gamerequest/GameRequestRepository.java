package org.victorpiles.escacs.api.gamerequest;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.victorpiles.escacs.api.user.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * {@link JpaRepository Repositori} relacionat amb les {@link GameRequest sol·licituds de joc}.
 *
 * @author Víctor Piles
 * @version 1.0
 */
@Repository
@Transactional(readOnly = true)
public interface GameRequestRepository extends JpaRepository<GameRequest, Long> {
    /**
     * Busca en la base de dades la {@link GameRequest sol·licitud de joc} amb
     * l'{@link GameRequest#getId() identificador} passat com a paràmetre.
     *
     * @param id L'{@link GameRequest#getId() identificador} de la {@link GameRequest sol·licitud de joc} a buscar.
     *
     * @return La {@link GameRequest sol·licitud de joc} amb l'{@link GameRequest#getId() identificador} passat com a
     * paràmetre, si aquesta existeix.
     */
    Optional<GameRequest> findById(UUID id);

    /**
     * Busca en la base de dades les {@link GameRequest sol·licituds de joc} amb
     * l'{@link GameRequest#getRequestingUser() emissor} passat com a paràmetre.
     *
     * @param user L'{@link GameRequest#getRequestingUser() usuari emissor} de la
     *             {@link GameRequest sol·licituds de joc} a buscar.
     *
     * @return Les {@link GameRequest sol·licituds de joc} amb l'{@link GameRequest#getRequestingUser() usuari emissor}
     * passat com a paràmetre.
     */
    List<GameRequest> findAllByRequestingUser(User user);

    /**
     * Busca en la base de dades les {@link GameRequest sol·licituds de joc} amb el
     * {@link GameRequest#getRequestedUser() receptor} passat com a paràmetre.
     *
     * @param user L'{@link GameRequest#getRequestedUser() usuari receptor} de la
     *             {@link GameRequest sol·licituds de joc} a buscar.
     *
     * @return Les {@link GameRequest sol·licituds de joc} amb l'{@link GameRequest#getRequestedUser() usuari receptor}
     * passat com a paràmetre.
     */
    List<GameRequest> findAllByRequestedUser(User user);

    /**
     * Busca en la base de dades les {@link GameRequest sol·licituds de joc} amb el
     * {@link GameRequest#getRequestedUser() receptor} i els estatus d'{@link GameRequest#isAccepted() acceptació} i
     * {@link GameRequest#isRejected() rebuig} passats com a paràmetres.
     *
     * @param requestedUser L'{@link GameRequest#getRequestedUser() usuari receptor} de la
     *                      {@link GameRequest sol·licituds de joc} a buscar.
     * @param accepted      L'estatus d'{@link GameRequest#isAccepted() acceptació}.
     * @param rejected      L'estatus de {@link GameRequest#isRejected() rebuig}.
     *
     * @return Les {@link GameRequest sol·licituds de joc} amb el {@link GameRequest#getRequestedUser() receptor} i els
     * estatus d'{@link GameRequest#isAccepted() acceptació} i {@link GameRequest#isRejected() rebuig} passats com a
     * paràmetres.
     */
    List<GameRequest> findAllByRequestedUserAndAcceptedAndRejected(User requestedUser, boolean accepted, boolean rejected);
}