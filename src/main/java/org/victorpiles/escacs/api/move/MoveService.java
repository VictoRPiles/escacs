package org.victorpiles.escacs.api.move;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.victorpiles.escacs.api.exception.game.GameEndedException;
import org.victorpiles.escacs.api.exception.game.GameNotFoundException;
import org.victorpiles.escacs.api.exception.move.InvalidMoveException;
import org.victorpiles.escacs.api.exception.user.UserNotInGameException;
import org.victorpiles.escacs.api.exception.user.UsernameNotFoundException;
import org.victorpiles.escacs.api.game.Game;
import org.victorpiles.escacs.api.game.GameRepository;
import org.victorpiles.escacs.api.user.User;
import org.victorpiles.escacs.api.user.UserRepository;
import org.victorpiles.escacs.engine.Engine;

import java.util.List;
import java.util.Optional;

/**
 * Lògica de negoci relacionada amb els {@link Move moviments}.
 *
 * @author Víctor Piles
 * @version 1.0
 */
@Log4j2
@Service
@Transactional
@AllArgsConstructor
public class MoveService {

    private final MoveRepository moveRepository;
    private final UserRepository userRepository;
    private final GameRepository gameRepository;

    /**
     * Busca tots els {@link Move moviments} presents a la base de dades.
     *
     * @return Un {@link List llistat} amb els {@link Move moviments} presents a la base de dades.
     */
    public List<Move> list() {
        return moveRepository.findAll();
    }

    /**
     * Busca tots els {@link Move moviments} executats per un {@link User usuari} presents a la base de dades.
     *
     * @param username {@link User#getUsername() Nom} del usuari.
     *
     * @return Un {@link List llistat} amb els {@link Move moviments} executats per un {@link User usuari} presents a la
     * base de dades.
     */
    public List<Move> listByUser(String username) {
        Optional<User> userByUsername = userRepository.findByUsername(username);
        if (userByUsername.isEmpty()) {
            throw new UsernameNotFoundException("We don't have an account for the username " + username + ". Try creating an account instead.");
        }

        List<Move> movesByUser = moveRepository.findAllByUser(userByUsername.get());

        return movesByUser.stream().toList();
    }

    /**
     * Busca tots els {@link Move moviments} executats a una {@link Game partida} en concret presents a la base de
     * dades.
     *
     * @param gameId L'{@link Game#getId() identificador} de la {@link Game partida}.
     *
     * @return Un {@link List llistat} amb els {@link Move moviments} executats a una {@link Game partida} en concret
     * presents a la base de dades.
     */
    public List<Move> listByGame(Long gameId) {
        Optional<Game> gameOptional = gameRepository.findById(gameId);
        if (gameOptional.isEmpty()) {
            throw new GameNotFoundException("Game " + gameId + " not found.");
        }

        List<Move> movesInGame = moveRepository.findAllByGame(gameOptional.get());

        return movesInGame.stream().toList();
    }

    /**
     * Registra un nou {@link Move moviment} en la base de dades.
     *
     * @param moveValue {@link Move#getValue() Valor} del {@link Move moviment}.
     * @param gameId    La {@link Game partida} on s'ha executat el moviment.
     * @param username  {@link User Usuari} que ha executat el {@link Move moviment}.
     *
     * @return La informació del {@link Move moviment} si s'ha registrat exitosament.
     */
    public Move execute(String moveValue, String context, Long gameId, String username) {
        Optional<User> userByUsername = userRepository.findByUsername(username);
        if (userByUsername.isEmpty()) {
            throw new UsernameNotFoundException("We don't have an account for the username " + username + ". Try creating an account instead.");
        }

        User user = userByUsername.get();

        Optional<Game> gameOptional = gameRepository.findById(gameId);
        if (gameOptional.isEmpty()) {
            throw new GameNotFoundException("Game " + gameId + " not found.");
        }

        Game game = gameOptional.get();
        if (game.isEnded()) {
            throw new GameEndedException("Game " + gameId + " has ended.");
        }

        if (!(game.getRequest().getRequestedUser().equals(user) || game.getRequest().getRequestingUser().equals(user))) {
            throw new UserNotInGameException("User " + username + " is not a player of the game " + gameId + ".");
        }

        Move move = new Move(moveValue, game, user);

        if (!Engine.isValidMove(move, context)) {
            throw new InvalidMoveException("Move " + move.getValue() + " is not valid.");
        }

        moveRepository.save(move);

        log.info("Executed: " + move.getValue() + " by " + user.getUsername() + " in game " + game.getId() + ".");
        return move;
    }

    public List<String> listValid(String piece, String context) {
        return Engine.getValidMoveValues(piece, context);
    }
}