package org.victorpiles.escacs.api.game;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.victorpiles.escacs.api.exception.game.GameNotFoundException;
import org.victorpiles.escacs.api.exception.gamerequest.GameRequestNotAcceptedException;
import org.victorpiles.escacs.api.exception.gamerequest.GameRequestNotFoundException;
import org.victorpiles.escacs.api.gamerequest.GameRequest;
import org.victorpiles.escacs.api.gamerequest.GameRequestRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Lògica de negoci relacionada amb les {@link Game partides}.
 *
 * @author Víctor Piles
 * @version 1.0
 */
@Log4j2
@Service
@Transactional
@AllArgsConstructor
public class GameService {

    private final @NotNull GameRepository gameRepository;
    private final @NotNull GameRequestRepository gameRequestRepository;

    /**
     * Busca totes les {@link Game partides} presents a la base de dades.
     *
     * @return Un {@link List llistat} amb les {@link Game partides} presents a la base de dades.
     */
    public @NotNull List<Game> list() {
        return gameRepository.findAll();
    }

    public @NotNull Game getByGameRequest(UUID gameRequestUUID) {
        Optional<GameRequest> gameRequestOptional = gameRequestRepository.findById(gameRequestUUID);
        if (gameRequestOptional.isEmpty()) {
            throw new GameRequestNotFoundException("Game request " + gameRequestUUID + " not found.");
        }

        GameRequest gameRequest = gameRequestOptional.get();
        if (!gameRequest.isAccepted() || gameRequest.isRejected()) {
            throw new GameRequestNotAcceptedException("Game request " + gameRequestUUID + " is not accepted yet.");
        }

        Optional<Game> gameOptional = gameRepository.findByRequest(gameRequest);
        if (gameOptional.isEmpty()) {
            throw new GameNotFoundException("Game with request " + gameRequestUUID + " not found.");
        }

        return gameOptional.get();
    }

    /**
     * Valida i registra a la base de dades una nova {@link Game partida} a partir de la
     * {@link GameRequest sol·licitud de joc} passada com a paràmetre.
     *
     * @param gameRequestUUID El id de {@link GameRequest sol·licitud de joc}.
     *
     * @return {@link GameRequest Nova partida} si s'ha creat amb èxit.
     */
    public @NotNull Game create(UUID gameRequestUUID) {
        Optional<GameRequest> gameRequestOptional = gameRequestRepository.findById(gameRequestUUID);
        if (gameRequestOptional.isEmpty()) {
            throw new GameRequestNotFoundException("Game request " + gameRequestUUID + " not found.");
        }

        GameRequest gameRequest = gameRequestOptional.get();
        if (!gameRequest.isAccepted() || gameRequest.isRejected()) {
            throw new GameRequestNotAcceptedException("Game request " + gameRequestUUID + " is not accepted yet.");
        }

        Game newGame = new Game(gameRequest);
        gameRepository.save(newGame);

        log.info("Game " + newGame.getId() + " created from request " + gameRequestUUID + ".");
        return newGame;
    }
}
