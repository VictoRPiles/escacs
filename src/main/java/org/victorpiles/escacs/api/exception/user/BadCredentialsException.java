package org.victorpiles.escacs.api.exception.user;

import org.victorpiles.escacs.api.user.User;
import org.victorpiles.escacs.api.user.UserService;

/**
 * Tipus d'{@link IllegalStateException excepció} utilitzada en la comprovació de credencials de l'{@link User usuari}.
 *
 * @author Víctor Piles
 * @version 1.0
 * @see UserService#login(String, String)
 */
public class BadCredentialsException extends IllegalStateException {

    public BadCredentialsException(String s) {
        super(s);
    }
}