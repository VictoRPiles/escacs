package org.victorpiles.escacs.api.move.stream;

import jakarta.validation.constraints.NotEmpty;
import jakarta.websocket.server.PathParam;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.victorpiles.escacs.api.game.Game;
import org.victorpiles.escacs.api.move.Move;
import org.victorpiles.escacs.api.move.MoveService;
import org.victorpiles.escacs.api.user.User;
import reactor.core.publisher.Flux;

/**
 * {@link Flux Fluxos} de dades relacionades amb els {@link Move moviments}.
 *
 * @author Víctor Piles
 * @version 1.0
 */
@RestController
@AllArgsConstructor
@RequestMapping(path = "/api/v1/move/stream")
public class MoveStreamController {

    private final @NotNull MoveService moveService;

    /**
     * Se subscriu a tots els {@link Move moviments} presents a la base de dades.
     *
     * @return Un {@link Flux flux} amb tots els {@link Move moviments} presents a la base de dades.
     */
    @GetMapping(path = "/all", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public @NotNull Flux<Move> all() {
        return Flux.fromIterable(moveService.list());
    }

    /**
     * Se subscriu als {@link Move moviments} d'un {@link User usuari} en concret presents a la base de dades.
     *
     * @param username {@link User#getUsername() Nom} del usuari.
     *
     * @return Un {@link Flux flux} amb els {@link Move moviments} d'un {@link User usuari} en concret presents a la
     * base de dades.
     */
    @GetMapping(path = "/byUser", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public @NotNull Flux<Move> byUser(@NotEmpty(message = "Username cannot be empty") @PathParam("username") String username) {
        return Flux.fromIterable(moveService.listByUser(username));
    }

    /**
     * Se subscriu als {@link Move moviments} d'una {@link Game partida} en concret presents a la base de dades.
     *
     * @param gameId L'{@link Game#getId() identificador} de la {@link Game partida}.
     *
     * @return Un {@link Flux flux} amb els {@link Move moviments} d'una {@link Game partida} en concret presents a la
     * base de dades.
     */
    @GetMapping(path = "/byGame", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public @NotNull Flux<Move> byGame(@PathParam("gameId") Long gameId) {
        return Flux.fromIterable(moveService.listByGame(gameId));
    }
}