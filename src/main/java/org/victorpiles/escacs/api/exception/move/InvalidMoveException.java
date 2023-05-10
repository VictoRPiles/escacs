package org.victorpiles.escacs.api.exception.move;

public class InvalidMoveException extends IllegalStateException {

    public InvalidMoveException(String s) {
        super(s);
    }
}
