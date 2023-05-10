package org.victorpiles.escacs.engine.square;

import lombok.NoArgsConstructor;
import lombok.Setter;
import org.victorpiles.escacs.engine.piece.Piece;

@Setter
@NoArgsConstructor
public class Square {

    private Piece piece;

    public Square(Piece piece) {
        this.piece = piece;
    }

    @Override
    public String toString() {
        return piece != null ? piece.toString() : "-";
    }
}
