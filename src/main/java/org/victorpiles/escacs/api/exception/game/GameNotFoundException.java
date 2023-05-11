package org.victorpiles.escacs.api.exception.game;

public class GameNotFoundException extends IllegalStateException {

    public GameNotFoundException(String s) {
        super(s);
    }
}
