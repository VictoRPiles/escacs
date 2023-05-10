package org.victorpiles.escacs.api.exception.gamerequest;

import org.victorpiles.escacs.api.gamerequest.GameRequest;
import org.victorpiles.escacs.api.user.User;

/**
 * Tipus d'{@link IllegalStateException excepció} utilitzada quan el {@link User usuari} no ha enviat cap
 * {@link GameRequest sol·licitud de joc}.
 *
 * @author Víctor Piles
 * @version 1.0
 */
public class UserHasNotSentGameRequestsException extends IllegalStateException {

    public UserHasNotSentGameRequestsException(String s) {
        super(s);
    }
}
