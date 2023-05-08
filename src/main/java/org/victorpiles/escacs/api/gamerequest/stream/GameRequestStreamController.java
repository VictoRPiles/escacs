package org.victorpiles.escacs.api.gamerequest.stream;

import jakarta.validation.constraints.NotEmpty;
import jakarta.websocket.server.PathParam;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.victorpiles.escacs.api.gamerequest.GameRequest;
import org.victorpiles.escacs.api.gamerequest.GameRequestService;
import org.victorpiles.escacs.api.user.User;
import reactor.core.publisher.Flux;

/**
 * {@link Flux Fluxos} de dades relacionades amb les {@link GameRequest sol·licituds de joc}
 *
 * @author Víctor Piles
 * @version 1.0
 */
@RestController
@AllArgsConstructor
@RequestMapping(path = "/api/v1/gameRequest/stream")
public class GameRequestStreamController {
    private final GameRequestService gameRequestService;

    /**
     * Se subscriu a les {@link GameRequest sol·licituds de joc} d'un {@link User usuari receptor} en concret presents a
     * la base de dades.
     *
     * @param username {@link User#getUsername() Nom} del usuari receptor.
     *
     * @return Un {@link Flux flux} amb les {@link GameRequest sol·licituds de joc} d'un {@link User usuari receptor} en
     * concret presents a la base de dades.
     */
    @GetMapping(path = "/to/user", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<GameRequest> toUser(@NotEmpty(message = "Username cannot be empty") @PathParam("username") String username) {
        return Flux.fromIterable(gameRequestService.listByRequestedUser(username));
    }
}