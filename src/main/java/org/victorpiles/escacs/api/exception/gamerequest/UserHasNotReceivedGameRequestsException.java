package org.victorpiles.escacs.api.exception.gamerequest;

import org.victorpiles.escacs.api.gamerequest.GameRequest;
import org.victorpiles.escacs.api.user.User;

/**
 * Tipus d'{@link IllegalStateException excepció} utilitzada quan el {@link User usuari} no ha rebut cap
 * {@link GameRequest sol·licitud de joc}.
 *
 * @author Víctor Piles
 * @version 1.0
 */
public class UserHasNotReceivedGameRequestsException extends IllegalStateException {
    public UserHasNotReceivedGameRequestsException(String message) {
        super(message);
    }
}
