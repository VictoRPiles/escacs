package org.victorpiles.escacs.api.move;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.victorpiles.escacs.api.exception.move.UserHasNoMovesException;
import org.victorpiles.escacs.api.exception.user.UsernameNotFoundException;
import org.victorpiles.escacs.api.user.User;
import org.victorpiles.escacs.api.user.UserRepository;

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
@AllArgsConstructor
public class MoveService {

    private final MoveRepository moveRepository;
    private final UserRepository userRepository;

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
        if (movesByUser.isEmpty()) {
            throw new UserHasNoMovesException("User " + username + " has no moves.");
        }

        return movesByUser.stream().toList();
    }

    /**
     * Registra un nou {@link Move moviment} en la base de dades.
     *
     * @param moveValue {@link Move#getValue() Valor} del {@link Move moviment}.
     * @param username  {@link User Usuari} que ha executat el {@link Move moviment}.
     *
     * @return La informació del {@link Move moviment} si s'ha registrat exitosament.
     */
    public Move execute(String moveValue, String username) {
        Optional<User> userByUsername = userRepository.findByUsername(username);
        if (userByUsername.isEmpty()) {
            throw new UsernameNotFoundException("We don't have an account for the username " + username + ". Try creating an account instead.");
        }

        Move move = new Move(moveValue, userByUsername.get());
        moveRepository.save(move);

        log.info("Executed: " + move);
        return move;
    }
}