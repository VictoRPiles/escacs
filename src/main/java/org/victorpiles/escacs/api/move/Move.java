package org.victorpiles.escacs.api.move;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.victorpiles.escacs.api.user.User;

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
     * {@link User Usuari} que ha executat el moviment.
     */
    @ManyToOne
    @NotNull(message = "User who has moved cannot be null")
    private User user;

    public Move(String value, User user) {
        this.value = value;
        this.user = user;
    }
}