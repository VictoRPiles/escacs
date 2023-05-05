package org.victorpiles.escacs.api.exception.move;

import org.victorpiles.escacs.api.move.Move;
import org.victorpiles.escacs.api.user.User;

/**
 * Tipus d'{@link IllegalStateException excepció} utilitzada quan el {@link User usuari} no té cap
 * {@link Move moviment}.
 *
 * @author Víctor Piles
 * @version 1.0
 */
public class UserHasNoMovesException extends IllegalStateException {

    public UserHasNoMovesException(String s) {
        super(s);
    }
}