package org.victorpiles.escacs.api.gamerequest;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.victorpiles.escacs.api.exception.gamerequest.*;
import org.victorpiles.escacs.api.exception.user.UsernameNotFoundException;
import org.victorpiles.escacs.api.user.User;
import org.victorpiles.escacs.api.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Lògica de negoci relacionada amb les {@link GameRequest sol·licituds de joc}.
 *
 * @author Víctor Piles
 * @version 1.0
 */
@Log4j2
@Service
@Transactional
@AllArgsConstructor
public class GameRequestService {

    private final GameRequestRepository gameRequestRepository;
    private final UserRepository userRepository;

    /**
     * Busca totes les {@link GameRequest sol·licituds de joc} presents a la base de dades.
     *
     * @return Un {@link List llistat} amb les {@link GameRequest sol·licituds de joc} presents a la base de dades.
     */
    public List<GameRequest> list() {
        return gameRequestRepository.findAll();
    }

    /**
     * Busca a la base de dades les {@link GameRequest sol·licituds de joc} d'un
     * {@link GameRequest#getRequestedUser() emissor} en concret.
     *
     * @return Un {@link List llistat} amb les {@link GameRequest sol·licituds de joc} presents a la base de dades.
     */
    public List<GameRequest> listByRequestingUser(String username) {
        Optional<User> userByUsername = userRepository.findByUsername(username);
        if (userByUsername.isEmpty()) {
            throw new UsernameNotFoundException("We don't have an account for the username " + username + ". Try creating an account instead.");
        }

        List<GameRequest> gameRequestByRequestingUser = gameRequestRepository.findAllByRequestingUser(userByUsername.get());
        if (gameRequestByRequestingUser.isEmpty()) {
            throw new UserHasNotSentGameRequestsException("User " + username + " has not sent game requests.");
        }

        return gameRequestByRequestingUser.stream().toList();
    }

    /**
     * Busca a la base de dades les {@link GameRequest sol·licituds de joc} d'un
     * {@link GameRequest#getRequestedUser() receptor} en concret.
     *
     * @return Un {@link List llistat} amb les {@link GameRequest sol·licituds de joc} presents a la base de dades.
     */
    public List<GameRequest> listByRequestedUser(String username) {
        Optional<User> userByUsername = userRepository.findByUsername(username);
        if (userByUsername.isEmpty()) {
            throw new UsernameNotFoundException("We don't have an account for the username " + username + ". Try creating an account instead.");
        }

        List<GameRequest> gameRequestByRequestedUser = gameRequestRepository.findAllByRequestedUser(userByUsername.get());
        if (gameRequestByRequestedUser.isEmpty()) {
            throw new UserHasNotReceivedGameRequestsException("User " + username + " has not received game requests.");
        }

        return gameRequestByRequestedUser.stream().toList();
    }

    /**
     * Valida i registra una nova {@link GameRequest sol·licitud de joc} a la base de dades.
     *
     * @param requestingUserUsername El {@link User#getUsername() nom d'usuari} del emissor.
     * @param requestedUserUsername  El {@link User#getUsername() nom d'usuari} del receptor.
     *
     * @return La {@link GameRequest sol·licitud de joc}, si s'ha enviat amb èxit.
     */
    public GameRequest send(String requestingUserUsername, String requestedUserUsername) {
        Optional<User> requestingUser = userRepository.findByUsername(requestingUserUsername);
        if (requestingUser.isEmpty()) {
            throw new UsernameNotFoundException("We don't have an account for the username " + requestingUserUsername + ". Try creating an account instead.");
        }
        Optional<User> requestedUser = userRepository.findByUsername(requestedUserUsername);
        if (requestedUser.isEmpty()) {
            throw new UsernameNotFoundException("We don't have an account for the username " + requestedUserUsername + ". Try creating an account instead.");
        }

        if (requestingUser.equals(requestedUser)) {
            throw new GameRequestForSelfException("Requesting user and requested user are the same.");
        }

        GameRequest gameRequest = new GameRequest(requestingUser.get(), requestedUser.get());
        gameRequestRepository.save(gameRequest);

        log.info("Game request sent: " + gameRequest.getId());
        return gameRequest;
    }

    /**
     * Valida i actualitza el camp {@link GameRequest#isAccepted() accepted} de la
     * {@link GameRequest sol·licitud de joc} a la base de dades.
     *
     * @param gameRequestUUID L'identificador del la {@link GameRequest sol·licitud de joc}
     *
     * @return La {@link GameRequest sol·licitud de joc}, si s'ha acceptat amb èxit.
     */
    public GameRequest accept(UUID gameRequestUUID) {
        Optional<GameRequest> gameRequestOptional = gameRequestRepository.findById(gameRequestUUID);
        if (gameRequestOptional.isEmpty()) {
            throw new GameRequestNotFoundException("Game request " + gameRequestUUID + " not found.");
        }

        GameRequest gameRequest = gameRequestOptional.get();
        if (gameRequest.isAccepted()) {
            throw new GameRequestAlreadyAcceptedException("Game request " + gameRequestUUID + " has already been accepted.");
        }
        if (gameRequest.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new GameRequestExpiredException("Game request " + gameRequestUUID + " has expired at " + gameRequest.getExpiresAt() + ".");
        }

        gameRequest.setAccepted(true);
        log.info("Game request accepted: " + gameRequestUUID);
        return gameRequest;
    }
}