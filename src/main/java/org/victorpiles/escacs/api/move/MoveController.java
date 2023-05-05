package org.victorpiles.escacs.api.move;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.victorpiles.escacs.api.user.User;

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

    private final MoveService moveService;

    /**
     * Genera una {@link ResponseEntity resposta} amb un {@link List llistat} tots els {@link Move moviments}.
     *
     * @return Un llistat tots els {@link Move moviments}.
     *
     * @see MoveService#list()
     */
    @GetMapping(path = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Move>> list() {
        List<Move> moveList = moveService.list();
        return ResponseEntity.ok(moveList);
    }

    /**
     * Genera una {@link ResponseEntity resposta} amb un {@link List llistat} tots els {@link Move moviments} executat
     * per un {@link User usuari} en concret.
     *
     * @param username El {@link User#getUsername() nom} de l'{@link User usuari}.
     *
     * @return Un llistat tots els {@link Move moviments} executat per un {@link User usuari}.
     *
     * @see MoveService#listByUser(String)
     */
    @GetMapping(path = "/list/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Move>> listByUser(@NotEmpty(message = "Username cannot be empty") @PathVariable String username) {
        List<Move> moveList = moveService.listByUser(username);
        return ResponseEntity.ok(moveList);
    }

    /**
     * Consumeix la informació per a executar un nou {@link Move moviment} i genera una {@link ResponseEntity resposta}
     * amb estatus 201 (created) si s'ha executat amb èxit.
     *
     * @param move     {@link Move#getValue() Valor} del {@link Move moviment}.
     * @param username {@link User Usuari} que ha executat el {@link Move moviment}.
     *
     * @see MoveService#execute(String, String)
     */
    @PostMapping(path = "/execute")
    public ResponseEntity<Move> execute(@RequestParam("move") String move, @RequestParam("username") @NotEmpty(message = "Username cannot be empty") String username) {
        Move executed = moveService.execute(move, username);

        return ResponseEntity
                .created(
                        ServletUriComponentsBuilder.fromCurrentRequest().path("/execute").buildAndExpand(executed).toUri()
                )
                .body(executed);
    }
}