package org.victorpiles.escacs.api.exception.user;

import org.victorpiles.escacs.api.user.User;

/**
 * Tipus d'{@link IllegalStateException excepció} utilitzada quan el {@link User#getEmail() correu} no existeix a la
 * base de dades.
 *
 * @author Víctor Piles
 * @version 1.0
 */
public class EmailNotFoundException extends IllegalStateException {

    public EmailNotFoundException(String s) {
        super(s);
    }
}