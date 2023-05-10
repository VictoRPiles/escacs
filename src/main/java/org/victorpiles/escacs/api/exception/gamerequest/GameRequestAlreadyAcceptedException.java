package org.victorpiles.escacs.api.exception.gamerequest;

import org.victorpiles.escacs.api.gamerequest.GameRequest;
import org.victorpiles.escacs.api.gamerequest.GameRequestService;

import java.util.UUID;

/**
 * Tipus d'{@link IllegalStateException excepció} utilitzada quan s'intenta
 * {@link GameRequestService#accept(UUID) acceptar} una {@link GameRequest sol·licitud de joc} que ja ha sigut
 * {@link GameRequest#isAccepted() acceptada}.
 *
 * @author Víctor Piles
 * @version 1.0
 */
public class GameRequestAlreadyAcceptedException extends IllegalStateException {

    public GameRequestAlreadyAcceptedException(String s) {
        super(s);
    }
}
