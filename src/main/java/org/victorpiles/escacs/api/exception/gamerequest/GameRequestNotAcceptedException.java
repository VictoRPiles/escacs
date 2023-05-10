package org.victorpiles.escacs.api.exception.gamerequest;

public class GameRequestNotAcceptedException extends IllegalStateException {

    public GameRequestNotAcceptedException(String s) {
        super(s);
    }
}
