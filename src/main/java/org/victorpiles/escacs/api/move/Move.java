package org.victorpiles.escacs.api.move;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.victorpiles.escacs.api.game.Game;
import org.victorpiles.escacs.api.user.User;
import org.victorpiles.escacs.engine.move.MoveStatus;

/**
 * Entitat encarregada de representar un moviment d'escacs.
 *
 * @author Víctor Piles
 * @version 1.0
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Move {

    /**
     * {@link Id Identificador} corresponent a moviment.
     * <p>
     * S'{@link GeneratedValue autoincrementa} de forma {@link SequenceGenerator seqüencial}.
     */
    @Id
    @SequenceGenerator(
            name = "move_sequence",
            sequenceName = "move_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "move_sequence"
    )
    private Long id;
    /**
     * Valor PGN del moviment.
     */
    @NotBlank(message = "Move value cannot be empty")
    private String value;
    /**
     * {@link Game Partida} on s'ha executat el moviment.
     */
    @ManyToOne
    @JoinColumn(
            nullable = false,
            name = "game_id"
    )
    private Game game;
    /**
     * {@link User Usuari} que ha executat el moviment.
     */
    @ManyToOne
    @JoinColumn(
            nullable = false,
            name = "user_id"
    )
    private User user;
    private MoveStatus status;

    public Move(String value) {
        this.value = value;
    }

    public Move(String value, Game game, User user) {
        this.value = value;
        this.game = game;
        this.user = user;
    }
}