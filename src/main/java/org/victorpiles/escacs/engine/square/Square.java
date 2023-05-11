package org.victorpiles.escacs.engine.square;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.victorpiles.escacs.engine.piece.Piece;

@Getter
@Setter
@NoArgsConstructor
public class Square {

    private Piece piece;

    public Square(Piece piece) {
        this.piece = piece;
    }

    public boolean isEmpty() {
        return piece == null;
    }

    public boolean isOccupied() {
        return piece != null;
    }

    public static boolean isValidPosition(int position) {
        return position >= 0 && position < 64;
    }

    @Override
    public String toString() {
        return piece != null ? piece.toString() : "-";
    }
}
