package org.victorpiles.escacs.engine.piece.type;

import org.victorpiles.escacs.engine.Board;
import org.victorpiles.escacs.engine.piece.Piece;
import org.victorpiles.escacs.engine.piece.alliance.PieceAlliance;
import org.victorpiles.escacs.engine.square.Square;
import org.victorpiles.escacs.engine.util.board.BoardUtils;
import org.victorpiles.escacs.engine.util.parser.MoveParser;

import java.util.ArrayList;
import java.util.List;

public class King extends Piece {

    public King(PieceType type, PieceAlliance alliance, int position) {
        super(type, alliance, position);
    }

    @Override
    public List<String> getValidMoveValues(Board board) {
        int[] offsets = {-9, -8, -7, -1, 1, 7, 8, 9};
        List<String> validMoveValues = new ArrayList<>();

        for (int offset : offsets) {
            int destination = position + offset;

            /* Casella fora dels límits del tauler -> moviment no vàlid */
            if (!Square.isValidPosition(destination)) {
                continue;
            }
            if (BoardUtils.FIRST_COLUMN.get(position) && ((offset == -9) || (offset == -1) || (offset == 7))) {
                continue;
            }
            if (BoardUtils.EIGHT_COLUMN.get(position) && ((offset == -7) || (offset == 1) || (offset == 9))) {
                continue;
            }

            Square destinationSquare = board.getSquareList().get(destination);
            if (destinationSquare.isOccupied()) {
                Piece attackedPiece = destinationSquare.getPiece();

                /* Casella ocupada amb peça amiga -> moviment no vàlid */
                if (alliance.equals(attackedPiece.getAlliance())) {
                    continue;
                }
            }

            /* Casella buida o ocupada amb peça enemiga -> moviment vàlid */
            validMoveValues.add(MoveParser.toMoveNotation(this, this.position, destination));
        }

        return validMoveValues;
    }
}
