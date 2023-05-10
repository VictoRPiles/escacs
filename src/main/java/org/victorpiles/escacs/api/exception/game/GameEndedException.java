package org.victorpiles.escacs.api.exception.game;

public class GameEndedException extends IllegalStateException {

    public GameEndedException(String s) {
        super(s);
    }
}
