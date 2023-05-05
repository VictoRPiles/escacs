package org.victorpiles.escacs.api.exception.user;

import org.victorpiles.escacs.api.user.User;

/**
 * Tipus d'{@link IllegalStateException excepció} utilitzada quan el {@link User#getUsername() nom d'usuari} ja existeix
 * a la base de dades.
 *
 * @author Víctor Piles
 * @version 1.0
 */
public class UsernameAlreadyInUseException extends IllegalStateException {

    public UsernameAlreadyInUseException(String s) {
        super(s);
    }
}