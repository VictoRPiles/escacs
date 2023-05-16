package org.victorpiles.escacs.api.user.stream;

import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.victorpiles.escacs.api.user.User;
import org.victorpiles.escacs.api.user.UserService;
import reactor.core.publisher.Flux;

/**
 * {@link Flux Fluxos} de dades relacionades amb els {@link User usuaris}.
 *
 * @author Víctor Piles
 * @version 1.0
 */
@RestController
@AllArgsConstructor
@RequestMapping(path = "/api/v1/user/stream")
public class UserStreamController {

    private final @NotNull UserService userService;

    /**
     * Se subscriu a tots els {@link User usuaris} presents a la base de dades.
     *
     * @return Un {@link Flux flux} amb tots els {@link User usuaris} presents a la base de dades.
     */
    @GetMapping(path = "/all", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public @NotNull Flux<User> all() {
        return Flux.fromIterable(userService.list());
    }
}