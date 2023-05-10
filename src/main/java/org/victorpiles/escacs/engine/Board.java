package org.victorpiles.escacs.engine;

import org.victorpiles.escacs.engine.piece.Piece;
import org.victorpiles.escacs.engine.square.Square;

import java.util.ArrayList;
import java.util.List;

public class Board {

    private final List<Square> squareList;

    public Board(List<Square> squareList) {
        this.squareList = squareList;
    }

    public void addPiece(Piece piece) {
        squareList.get(piece.getPosition()).setPiece(piece);
    }

    public static Board build() {
        ArrayList<Square> squares = new ArrayList<>();
        for (int i = 0; i < 64; i++) {
            squares.add(new Square());
        }

        return new Board(squares);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < squareList.size(); i++) {
            if ((i != 0) && (i % 8 == 0)) {
                sb.append("\n");
            }

            sb.append(squareList.get(i)).append("  ");
        }

        return sb.toString();
    }
}
