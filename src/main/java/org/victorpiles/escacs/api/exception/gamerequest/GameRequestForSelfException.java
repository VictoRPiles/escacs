package org.victorpiles.escacs.api.exception.gamerequest;

import org.victorpiles.escacs.api.gamerequest.GameRequest;
import org.victorpiles.escacs.api.gamerequest.GameRequestService;
import org.victorpiles.escacs.api.user.User;

/**
 * Tipus d'{@link IllegalStateException excepció} utilitzada quan s'intenta
 * {@link GameRequestService#send(String, String) enviar} una {@link GameRequest sol·licitud de joc} amb el mateix
 * {@link User usuari} emissor i receptor.
 *
 * @author Víctor Piles
 * @version 1.0
 */
public class GameRequestForSelfException extends IllegalStateException {
    public GameRequestForSelfException(String s) {
        super(s);
    }
}
