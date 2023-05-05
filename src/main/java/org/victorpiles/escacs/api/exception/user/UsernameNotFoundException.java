package org.victorpiles.escacs.api.exception.user;

import org.victorpiles.escacs.api.user.User;

/**
 * Tipus d'{@link IllegalStateException excepció} utilitzada quan el {@link User#getUsername() nom d'usuari} no existeix
 * a la base de dades.
 *
 * @author Víctor Piles
 * @version 1.0
 */
public class UsernameNotFoundException extends IllegalStateException {

    public UsernameNotFoundException(String s) {
        super(s);
    }
}