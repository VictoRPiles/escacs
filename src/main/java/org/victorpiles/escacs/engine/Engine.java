package org.victorpiles.escacs.engine;

import org.victorpiles.escacs.api.move.Move;
import org.victorpiles.escacs.engine.move.MoveStatus;
import org.victorpiles.escacs.engine.piece.Piece;
import org.victorpiles.escacs.engine.util.parser.ContextParser;
import org.victorpiles.escacs.engine.util.parser.PieceParser;

import java.util.List;

public class Engine {

    public static boolean isValidMove(Move move, String context) {
        Board board = ContextParser.parse(context);
        MoveStatus status = board.execute(move);

        return status.ok();
    }

    public static List<String> getValidMoveValues(String pieceNotation, String context) {
        Board board = ContextParser.parse(context);
        Piece piece = PieceParser.parsePiece(pieceNotation);

        return piece.getValidMoveValues(board);
    }
}
