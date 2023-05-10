package org.victorpiles.escacs.engine.piece;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.victorpiles.escacs.engine.piece.alliance.PieceAlliance;
import org.victorpiles.escacs.engine.piece.type.PieceType;

@Getter
@AllArgsConstructor
public abstract class Piece {

    private final PieceType type;
    private final PieceAlliance alliance;
    private int position;

    @Override
    public String toString() {
        return alliance.isLight() ? type.getNotation().toUpperCase() : type.getNotation().toLowerCase();
    }
}
