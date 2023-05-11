package org.victorpiles.escacs.api.exception.gamerequest;

public class GameRequestAlreadyRejectedException extends IllegalStateException {

    public GameRequestAlreadyRejectedException(String s) {
        super(s);
    }
}
