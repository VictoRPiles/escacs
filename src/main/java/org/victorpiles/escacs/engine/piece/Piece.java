package org.victorpiles.escacs.engine.piece;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.victorpiles.escacs.engine.Board;
import org.victorpiles.escacs.engine.piece.alliance.PieceAlliance;
import org.victorpiles.escacs.engine.piece.type.PieceType;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public abstract class Piece {

    protected final PieceType type;
    protected final PieceAlliance alliance;
    protected int position;

    public abstract List<String> getValidMoveValues(Board board);

    public boolean hasValidMoves(Board board) {
        return !getValidMoveValues(board).isEmpty();
    }

    @Override
    public String toString() {
        return alliance.isLight() ? type.getNotation().toUpperCase() : type.getNotation().toLowerCase();
    }
}
