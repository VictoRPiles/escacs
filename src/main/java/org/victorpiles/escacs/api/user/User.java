package org.victorpiles.escacs.api.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.victorpiles.escacs.api.security.PasswordEncoder;

/**
 * Entitat encarregada de representar un usuari.
 *
 * @author Víctor Piles
 * @version 1.0
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    /**
     * {@link Id Identificador} corresponent a usuari.
     * <p>
     * S'{@link GeneratedValue autoincrementa} de forma {@link SequenceGenerator seqüencial}.
     */
    @Id
    @SequenceGenerator(
            name = "user_sequence",
            sequenceName = "user_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_sequence"
    )
    private Long id;
    /**
     * Nom o "nickname" corresponent a usuari.
     * <p>
     * Ha de ser un valor {@link Column#unique() únic}.
     */
    @NotBlank(message = "Username cannot be empty")
    @Column(unique = true)
    private String username;
    /**
     * Email corresponent a l'usuari.
     * <p>
     * Ha de complir les {@link Email normes de validació}.
     * <p>
     * Ha de ser un valor {@link Column#unique() únic}.
     */
    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Invalid email ${validatedValue}")
    @Column(unique = true)
    private String email;
    /**
     * Contrasenya corresponent a l'usuari.
     *
     * @see PasswordEncoder#encode(String) No s'ha de guardar en la base de dades com a text clar.
     * @see JsonProperty No quedarà exposada en serialitzar l'objecte a JSON.
     */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank(message = "Password cannot be empty")
    private String password;
    /**
     * Puntuació al ranking multijugador corresponent l'usuari.
     *
     * @see User#initializeScore() Conté el valor 0 quan es registra l'usuari.
     */
    private Integer score;

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    /**
     * Inicialitza la {@link User#score puntuació} a 0 quan es registra l'usuari.
     */
    @PrePersist
    private void initializeScore() {
        this.score = 0;
    }
}