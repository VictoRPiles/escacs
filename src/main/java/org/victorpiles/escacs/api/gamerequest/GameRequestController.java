package org.victorpiles.escacs.api.gamerequest;

import jakarta.validation.constraints.NotEmpty;
import jakarta.websocket.server.PathParam;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.victorpiles.escacs.api.user.User;

import java.util.List;
import java.util.UUID;

/**
 * Endpoints relacionats amb les {@link GameRequest sol·licituds de joc}.
 *
 * @author Víctor Piles
 * @version 1.0
 */
@RestController
@AllArgsConstructor
@RequestMapping(path = "/api/v1/gameRequest")
public class GameRequestController {

    private final @NotNull GameRequestService gameRequestService;

    /**
     * Genera una {@link ResponseEntity resposta} amb un {@link List llistat} de totes les
     * {@link GameRequest sol·licituds de joc}.
     *
     * @return Un amb llistat totes les {@link GameRequest sol·licituds de joc}.
     *
     * @see GameRequestService#list()
     */
    @GetMapping(path = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public @NotNull ResponseEntity<List<GameRequest>> list() {
        List<GameRequest> gameRequestList = gameRequestService.list();
        return ResponseEntity.ok(gameRequestList);
    }

    /**
     * Genera una {@link ResponseEntity resposta} amb un {@link List llistat} de les
     * {@link GameRequest sol·licituds de joc} d'un {@link GameRequest#getRequestingUser() emissor} en concret.
     *
     * @return Un amb llistat totes les {@link GameRequest sol·licituds de joc} d'un
     * {@link GameRequest#getRequestingUser() emissor} en concret.
     *
     * @see GameRequestService#listByRequestingUser(String)
     */
    @GetMapping(path = "/list/from", produces = MediaType.APPLICATION_JSON_VALUE)
    public @NotNull ResponseEntity<List<GameRequest>> listByRequestingUser(@NotEmpty(message = "Username cannot be empty") @PathParam("username") String username) {
        List<GameRequest> gameRequestList = gameRequestService.listByRequestingUser(username);
        return ResponseEntity.ok(gameRequestList);
    }

    /**
     * Genera una {@link ResponseEntity resposta} amb un {@link List llistat} de les
     * {@link GameRequest sol·licituds de joc} d'un {@link GameRequest#getRequestedUser() receptor} en concret.
     *
     * @return Un amb llistat totes les {@link GameRequest sol·licituds de joc} d'un
     * {@link GameRequest#getRequestedUser() receptor} en concret.
     *
     * @see GameRequestService#listByRequestedUser(String)
     */
    @GetMapping(path = "/list/to", produces = MediaType.APPLICATION_JSON_VALUE)
    public @NotNull ResponseEntity<List<GameRequest>> listByRequestedUser(@NotEmpty(message = "Username cannot be empty") @PathParam("username") String username) {
        List<GameRequest> gameRequestList = gameRequestService.listByRequestedUser(username);
        return ResponseEntity.ok(gameRequestList);
    }

    /**
     * Genera una {@link ResponseEntity resposta} amb un {@link List llistat} de les
     * {@link GameRequest sol·licituds de joc} pendents d'un {@link GameRequest#getRequestedUser() receptor} en
     * concret.
     *
     * @return Un amb llistat totes les {@link GameRequest sol·licituds de joc} pendents d'un
     * {@link GameRequest#getRequestedUser() receptor} en concret.
     *
     * @see GameRequestService#listByRequestedUser(String)
     */
    @GetMapping(path = "/pending/to", produces = MediaType.APPLICATION_JSON_VALUE)
    public @NotNull ResponseEntity<List<GameRequest>> pendingByRequestedUser(@NotEmpty(message = "Username cannot be empty") @PathParam("username") String username) {
        List<GameRequest> gameRequestList = gameRequestService.pendingByRequestedUser(username);
        return ResponseEntity.ok(gameRequestList);
    }

    /**
     * Consumeix la informació per a enviar una nova {@link GameRequest sol·licitud de joc} i genera una
     * {@link ResponseEntity resposta} amb estatus 201 (created) si s'ha enviat amb èxit.
     *
     * @param requestingUserUsername El {@link User#getUsername() nom d'usuari} del emissor.
     * @param requestedUserUsername  El {@link User#getUsername() nom d'usuari} del receptor.
     *
     * @return La {@link GameRequest sol·licitud de joc}, si s'ha enviat amb èxit.
     *
     * @see GameRequestService#send(String, String)
     */
    @PostMapping(path = "/send", produces = MediaType.APPLICATION_JSON_VALUE)
    public @NotNull ResponseEntity<GameRequest> send(
            @RequestParam("requestingUserUsername") String requestingUserUsername,
            @RequestParam("requestedUserUsername") String requestedUserUsername
    ) {
        GameRequest sent = gameRequestService.send(requestingUserUsername, requestedUserUsername);
        return ResponseEntity
                .created(
                        ServletUriComponentsBuilder.fromCurrentRequest().path("/register").buildAndExpand(sent).toUri()
                )
                .body(sent);
    }

    /**
     * Marca la {@link GameRequest sol·licitud de joc} com {@link GameRequest#isAccepted() acceptada}.
     *
     * @param gameRequestUUID L'identificador del la {@link GameRequest sol·licitud de joc}
     *
     * @return La {@link GameRequest sol·licitud de joc}, si s'ha acceptat amb èxit.
     *
     * @see GameRequestService#accept(UUID)
     */
    @PutMapping(path = "/accept", produces = MediaType.APPLICATION_JSON_VALUE)
    public @NotNull ResponseEntity<GameRequest> accept(@RequestParam("uuid") UUID gameRequestUUID) {
        GameRequest accepted = gameRequestService.accept(gameRequestUUID);
        return ResponseEntity.ok(accepted);
    }

    /**
     * Marca la {@link GameRequest sol·licitud de joc} com {@link GameRequest#isRejected() rebutjada}.
     *
     * @param gameRequestUUID L'identificador del la {@link GameRequest sol·licitud de joc}
     *
     * @return La {@link GameRequest sol·licitud de joc}, si s'ha rebutjat amb èxit.
     *
     * @see GameRequestService#reject(UUID)
     */
    @PutMapping(path = "/reject", produces = MediaType.APPLICATION_JSON_VALUE)
    public @NotNull ResponseEntity<GameRequest> reject(@RequestParam("uuid") UUID gameRequestUUID) {
        GameRequest accepted = gameRequestService.reject(gameRequestUUID);
        return ResponseEntity.ok(accepted);
    }
}