package org.victorpiles.escacs.api.exception.gamerequest;

import org.victorpiles.escacs.api.gamerequest.GameRequest;

/**
 * Tipus d'{@link IllegalStateException excepció} utilitzada quan la {@link GameRequest sol·licitud de joc} no existeix
 * a la base de dades.
 *
 * @author Víctor Piles
 * @version 1.0
 */
public class GameRequestNotFoundException extends IllegalStateException {

    public GameRequestNotFoundException(String s) {
        super(s);
    }
}
