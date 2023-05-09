package org.victorpiles.escacs.api.game;

import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.victorpiles.escacs.api.gamerequest.GameRequest;

import java.util.List;
import java.util.UUID;

/**
 * Endpoints relacionats amb les {@link Game partides}.
 *
 * @author Víctor Piles
 * @version 1.0
 */
@RestController
@AllArgsConstructor
@RequestMapping(path = "/api/v1/game")
public class GameController {

    private final GameService gameService;

    /**
     * Genera una {@link ResponseEntity resposta} amb un {@link List llistat} totes les {@link Game partides}.
     *
     * @return Un llistat otes les {@link Game partides}.
     *
     * @see GameService#list()
     */
    @GetMapping(path = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Game>> list() {
        List<Game> gameList = gameService.list();
        return ResponseEntity.ok(gameList);
    }

    /**
     * Genera una {@link ResponseEntity resposta} amb un {@link List llistat} totes les {@link Game partides}.
     *
     * @return Un llistat otes les {@link Game partides}.
     *
     * @see GameService#list()
     */
    @GetMapping(path = "/listByGameRequest", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Game> getByGameRequest(@RequestParam UUID gameRequestUUID) {
        Game gameByRequest = gameService.getByGameRequest(gameRequestUUID);
        return ResponseEntity.ok(gameByRequest);
    }

    /**
     * Crea una nova {@link Game partida} a partir de la {@link GameRequest sol·licitud de joc} passada com a
     * paràmetre.
     *
     * @param gameRequestUUID El id de {@link GameRequest sol·licitud de joc}.
     *
     * @return {@link ResponseEntity Resposta} amb estatus 201 (created) si s'ha creat amb èxit.
     *
     * @see GameService#create(UUID)
     */
    @PostMapping(path = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Game> create(@RequestParam UUID gameRequestUUID) {
        Game created = gameService.create(gameRequestUUID);
        return ResponseEntity
                .created(
                        ServletUriComponentsBuilder.fromCurrentRequest().path("/create").buildAndExpand(created).toUri()
                )
                .body(created);
    }
}