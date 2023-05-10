package org.victorpiles.escacs.engine.piece.type;

import org.victorpiles.escacs.engine.Board;
import org.victorpiles.escacs.engine.piece.Piece;
import org.victorpiles.escacs.engine.piece.alliance.PieceAlliance;

import java.util.List;

public class Pawn extends Piece {

    public Pawn(PieceType type, PieceAlliance alliance, int position) {
        super(type, alliance, position);
    }

    @Override
    public List<String> getValidMoveValues(Board board) {
        // TODO: 10/5/23 Pawn.getValidMoves
        return null;
    }
}
