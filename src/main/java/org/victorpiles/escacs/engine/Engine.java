package org.victorpiles.escacs.engine;

import org.victorpiles.escacs.api.move.Move;
import org.victorpiles.escacs.engine.move.MoveStatus;
import org.victorpiles.escacs.engine.piece.Piece;
import org.victorpiles.escacs.engine.piece.alliance.PieceAlliance;
import org.victorpiles.escacs.engine.util.parser.ContextParser;
import org.victorpiles.escacs.engine.util.parser.PieceParser;

import java.util.List;

public class Engine {

    public static MoveStatus isValidMove(Move move, String context) {
        Board board = ContextParser.parse(context);
        return board.execute(move.getValue(), true);
    }

    public static List<String> getValidMoveValues(String pieceNotation, String context) {
        Board board = ContextParser.parse(context);
        Piece piece = PieceParser.parsePiece(pieceNotation);

        return piece.getValidMoveValues(board);
    }

    public static PieceAlliance getPieceAlliance(String pieceNotation) {
        Piece piece = PieceParser.parsePiece(pieceNotation);

        return piece.getAlliance();
    }
}
