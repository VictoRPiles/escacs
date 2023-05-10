package org.victorpiles.escacs.api.game;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.victorpiles.escacs.api.gamerequest.GameRequest;
import org.victorpiles.escacs.api.user.User;

/**
 * Entitat encarregada de representar una partida d'escacs.
 *
 * @author Víctor Piles
 * @version 1.0
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Game {

    /**
     * {@link Id Identificador} corresponent a la partida.
     * <p>
     * S'{@link GeneratedValue autoincrementa} de forma {@link SequenceGenerator seqüencial}.
     */
    @Id
    @SequenceGenerator(
            name = "game_sequence",
            sequenceName = "game_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "game_sequence"
    )
    private Long id;

    /**
     * {@link GameRequest Sol·licitud de joc} que ha generat aquesta partida.
     */
    @OneToOne
    @JoinColumn(
            nullable = false,
            name = "game_request_id"
    )
    private GameRequest request;
    /**
     * Indica si la partida ha finalitzat o no.
     */
    private boolean ended;
    /**
     * {@link User Usuari} guanyador de la partida.
     */
    @ManyToOne
    @JoinColumn(
            name = "winner_id"
    )
    private User winner;

    public Game(GameRequest request) {
        this.request = request;
    }

    @PrePersist
    private void initializeGame() {
        this.ended = false;
    }
}
