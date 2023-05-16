package org.victorpiles.escacs.api.move;

import jakarta.validation.constraints.NotEmpty;
import jakarta.websocket.server.PathParam;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.victorpiles.escacs.api.game.Game;
import org.victorpiles.escacs.api.user.User;
import org.victorpiles.escacs.engine.move.MoveStatus;

import java.util.List;

/**
 * Endpoints relacionats amb els {@link Move moviments}.
 *
 * @author Víctor Piles
 * @version 1.0
 */
@RestController
@AllArgsConstructor
@RequestMapping(path = "/api/v1/move")
public class MoveController {

    private final @NotNull MoveService moveService;

    /**
     * Genera una {@link ResponseEntity resposta} amb un {@link List llistat} de tots els {@link Move moviments}.
     *
     * @return Un llistat amb tots els {@link Move moviments}.
     *
     * @see MoveService#list()
     */
    @GetMapping(path = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public @NotNull ResponseEntity<List<Move>> list() {
        List<Move> moveList = moveService.list();
        return ResponseEntity.ok(moveList);
    }

    /**
     * Genera una {@link ResponseEntity resposta} amb un {@link List llistat} de tots els {@link Move moviments}
     * executat per un {@link User usuari} en concret.
     *
     * @param username El {@link User#getUsername() nom} de l'{@link User usuari}.
     *
     * @return Un llistat amb tots els {@link Move moviments} executat per un {@link User usuari}.
     *
     * @see MoveService#listByUser(String)
     */
    @GetMapping(path = "/list/byUser", produces = MediaType.APPLICATION_JSON_VALUE)
    public @NotNull ResponseEntity<List<Move>> listByUser(@NotEmpty(message = "Username cannot be empty") @PathParam("username") String username) {
        List<Move> moveList = moveService.listByUser(username);
        return ResponseEntity.ok(moveList);
    }

    /**
     * Consumeix la informació per a executar un nou {@link Move moviment} i genera una {@link ResponseEntity resposta}
     * amb estatus 201 (created) si s'ha executat amb èxit.
     *
     * @param move     {@link Move#getValue() Valor} del {@link Move moviment}.
     * @param gameId   La {@link Game partida} on s'ha executat el moviment.
     * @param username {@link User Usuari} que ha executat el {@link Move moviment}.
     *
     * @see MoveService#execute(String, String, Long, String)
     */
    @PostMapping(path = "/execute")
    public @NotNull ResponseEntity<MoveStatus> execute(@RequestParam("move") String move, @RequestParam("context") String context, @RequestParam("gameId") @NotNull Long gameId, @RequestParam("username") @NotEmpty(message = "Username cannot be empty") String username) {
        MoveStatus status = moveService.execute(move, context, gameId, username);

        return ResponseEntity
                .created(
                        ServletUriComponentsBuilder.fromCurrentRequest().path("/execute").buildAndExpand(status).toUri()
                )
                .body(status);
    }

    @PostMapping(path = "/listValid")
    public @NotNull ResponseEntity<List<String>> listValid(@RequestParam("piece") String piece, @RequestParam("context") String context) {
        List<String> validMoves = moveService.listValid(piece, context);

        return ResponseEntity.ok(validMoves);
    }
}