package org.victorpiles.escacs.api.gamerequest;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.victorpiles.escacs.api.user.User;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entitat encarregada de representar una sol·licitud de joc.
 *
 * @author Víctor Piles
 * @version 1.0
 */

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameRequest {

    /**
     * {@link Id Identificador} corresponent a la sol·licitud de joc.
     * <p>
     * S'{@link GeneratedValue autogenera} un nou {@link UUID uuid}.
     */
    @Id
    @GeneratedValue(
            strategy = GenerationType.UUID
    )
    private UUID id;
    /**
     * {@link User Usuari} emissor de la sol·licitud de joc.
     */
    @ManyToOne
    @JoinColumn(
            nullable = false,
            name = "requesting_user_id"
    )
    private User requestingUser;
    /**
     * {@link User Usuari} receptor de la sol·licitud de joc.
     */
    @ManyToOne
    @JoinColumn(
            nullable = false,
            name = "requested_user_id"
    )
    private User requestedUser;
    /**
     * {@link LocalDateTime moment} de l'emissió de la sol·licitud de joc.
     */
    @Column(nullable = false)
    @JsonFormat(pattern = "HH:mm")
    private LocalDateTime requestedAt;
    /**
     * {@link LocalDateTime moment} d'expiració de la sol·licitud de joc.
     */
    private LocalDateTime expiresAt;
    /**
     * Indica si la sol·licitud de joc ha segut o no acceptada pel receptor.
     */
    private boolean accepted;

    public GameRequest(User requestingUser, User requestedUser) {
        this.requestingUser = requestingUser;
        this.requestedUser = requestedUser;
    }

    /**
     * S'executa quan es registra la sol·licitud de joc
     * <p>
     * Estableix el {@link GameRequest#requestedAt moment d'emissió} i el {@link GameRequest#expiresAt d'expiració}.
     * <p>
     * Marca la sol·licitud com a no {@link GameRequest#accepted accpetada}.
     */
    @PrePersist
    private void initializeRequest() {
        this.requestedAt = LocalDateTime.now();
        this.expiresAt = requestedAt.plusMinutes(15);
        this.accepted = false;
    }
}