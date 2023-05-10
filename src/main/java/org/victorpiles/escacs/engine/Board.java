package org.victorpiles.escacs.engine;

import lombok.Getter;
import org.victorpiles.escacs.api.move.Move;
import org.victorpiles.escacs.engine.move.MoveStatus;
import org.victorpiles.escacs.engine.piece.Piece;
import org.victorpiles.escacs.engine.square.Square;
import org.victorpiles.escacs.engine.util.parser.MoveParser;

import java.util.ArrayList;
import java.util.List;

@Getter
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

    public MoveStatus execute(Move move) {
        int origin = MoveParser.parsePieceOriginPosition(move);
        Piece movedPiece = squareList.get(origin).getPiece();

        List<String> validMoveValues = movedPiece.getValidMoveValues(this);
        if (!validMoveValues.contains(move.getValue())) {
            return MoveStatus.KO;
        }

        // TODO: 10/5/23 Escac i escac mat

        return MoveStatus.OK;
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
