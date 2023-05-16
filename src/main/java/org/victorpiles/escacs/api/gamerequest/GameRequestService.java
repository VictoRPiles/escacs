package org.victorpiles.escacs.api.gamerequest;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
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

    private final @NotNull GameRequestRepository gameRequestRepository;
    private final @NotNull UserRepository userRepository;

    /**
     * Busca totes les {@link GameRequest sol·licituds de joc} presents a la base de dades.
     *
     * @return Un {@link List llistat} amb les {@link GameRequest sol·licituds de joc} presents a la base de dades.
     */
    public @NotNull List<GameRequest> list() {
        return gameRequestRepository.findAll();
    }

    /**
     * Busca a la base de dades les {@link GameRequest sol·licituds de joc} d'un
     * {@link GameRequest#getRequestedUser() emissor} en concret.
     *
     * @return Un {@link List llistat} amb les {@link GameRequest sol·licituds de joc} presents a la base de dades.
     */
    public @NotNull List<GameRequest> listByRequestingUser(String username) {
        Optional<User> userByUsername = userRepository.findByUsername(username);
        if (userByUsername.isEmpty()) {
            throw new UsernameNotFoundException("We don't have an account for the username " + username + ". Try creating an account instead.");
        }

        List<GameRequest> gameRequestByRequestingUser = gameRequestRepository.findAllByRequestingUser(userByUsername.get());

        return gameRequestByRequestingUser.stream().toList();
    }

    /**
     * Busca a la base de dades les {@link GameRequest sol·licituds de joc} d'un
     * {@link GameRequest#getRequestedUser() receptor} en concret.
     *
     * @return Un {@link List llistat} amb les {@link GameRequest sol·licituds de joc} presents a la base de dades.
     */
    public @NotNull List<GameRequest> listByRequestedUser(String username) {
        Optional<User> userByUsername = userRepository.findByUsername(username);
        if (userByUsername.isEmpty()) {
            throw new UsernameNotFoundException("We don't have an account for the username " + username + ". Try creating an account instead.");
        }

        List<GameRequest> gameRequestByRequestedUser = gameRequestRepository.findAllByRequestedUser(userByUsername.get());

        return gameRequestByRequestedUser.stream().toList();
    }

    /**
     * Busca a la base de dades les {@link GameRequest sol·licituds de joc} pendents d'un
     * {@link GameRequest#getRequestedUser() receptor} en concret.
     *
     * @return Un {@link List llistat} amb les {@link GameRequest sol·licituds de joc} presents a la base de dades.
     */
    public @NotNull List<GameRequest> pendingByRequestedUser(String username) {
        Optional<User> userByUsername = userRepository.findByUsername(username);
        if (userByUsername.isEmpty()) {
            throw new UsernameNotFoundException("We don't have an account for the username " + username + ". Try creating an account instead.");
        }

        List<GameRequest> pendingGameRequestByRequestedUser = gameRequestRepository.findAllByRequestedUserAndAcceptedAndRejected(userByUsername.get(), false, false);

        return pendingGameRequestByRequestedUser.stream().toList();
    }

    /**
     * Valida i registra una nova {@link GameRequest sol·licitud de joc} a la base de dades.
     *
     * @param requestingUserUsername El {@link User#getUsername() nom d'usuari} del emissor.
     * @param requestedUserUsername  El {@link User#getUsername() nom d'usuari} del receptor.
     *
     * @return La {@link GameRequest sol·licitud de joc}, si s'ha enviat amb èxit.
     */
    public @NotNull GameRequest send(String requestingUserUsername, String requestedUserUsername) {
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
    public @NotNull GameRequest accept(UUID gameRequestUUID) {
        Optional<GameRequest> gameRequestOptional = gameRequestRepository.findById(gameRequestUUID);
        if (gameRequestOptional.isEmpty()) {
            throw new GameRequestNotFoundException("Game request " + gameRequestUUID + " not found.");
        }

        GameRequest gameRequest = gameRequestOptional.get();
        if (gameRequest.isAccepted()) {
            throw new GameRequestAlreadyAcceptedException("Game request " + gameRequestUUID + " has already been accepted.");
        }
        if (gameRequest.isRejected()) {
            throw new GameRequestAlreadyRejectedException("Game request " + gameRequestUUID + " has been rejected.");
        }
        if (gameRequest.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new GameRequestExpiredException("Game request " + gameRequestUUID + " has expired at " + gameRequest.getExpiresAt() + ".");
        }

        gameRequest.setAccepted(true);
        gameRequest.setAcceptedAt(LocalDateTime.now());
        log.info("Game request accepted: " + gameRequestUUID);
        return gameRequest;
    }

    /**
     * Valida i actualitza el camp {@link GameRequest#isRejected() rejected} de la
     * {@link GameRequest sol·licitud de joc} a la base de dades.
     *
     * @param gameRequestUUID L'identificador del la {@link GameRequest sol·licitud de joc}
     *
     * @return La {@link GameRequest sol·licitud de joc}, si s'ha rebutjat amb èxit.
     */
    public @NotNull GameRequest reject(UUID gameRequestUUID) {
        Optional<GameRequest> gameRequestOptional = gameRequestRepository.findById(gameRequestUUID);
        if (gameRequestOptional.isEmpty()) {
            throw new GameRequestNotFoundException("Game request " + gameRequestUUID + " not found.");
        }

        GameRequest gameRequest = gameRequestOptional.get();
        if (gameRequest.isAccepted()) {
            throw new GameRequestAlreadyRejectedException("Game request " + gameRequestUUID + " has already been rejected.");
        }
        if (gameRequest.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new GameRequestExpiredException("Game request " + gameRequestUUID + " has expired at " + gameRequest.getExpiresAt() + ".");
        }

        gameRequest.setRejected(true);
        log.info("Game request rejected: " + gameRequestUUID);
        return gameRequest;
    }
}