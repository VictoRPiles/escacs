package org.victorpiles.escacs.engine.piece;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.victorpiles.escacs.engine.Board;
import org.victorpiles.escacs.engine.piece.alliance.PieceAlliance;
import org.victorpiles.escacs.engine.piece.type.PieceType;

import java.util.List;

@Getter
@AllArgsConstructor
public abstract class Piece {

    protected final PieceType type;
    protected final PieceAlliance alliance;
    protected int position;

    public abstract List<String> getValidMoveValues(Board board);

    @Override
    public String toString() {
        return alliance.isLight() ? type.getNotation().toUpperCase() : type.getNotation().toLowerCase();
    }
}
