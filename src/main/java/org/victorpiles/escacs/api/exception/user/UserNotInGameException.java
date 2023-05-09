package org.victorpiles.escacs.api.exception.user;

public class UserNotInGameException extends IllegalStateException {
    public UserNotInGameException(String s) {
        super(s);
    }
}
