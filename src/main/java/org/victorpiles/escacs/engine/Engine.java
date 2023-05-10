package org.victorpiles.escacs.engine;

import org.victorpiles.escacs.api.move.Move;
import org.victorpiles.escacs.engine.util.ContextParser;

public class Engine {

    public static boolean isValidMove(Move move, String context) {
        ContextParser.parse(context);

        return true;
    }
}
