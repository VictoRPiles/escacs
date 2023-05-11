package org.victorpiles.escacs.engine.piece.type;

import lombok.extern.log4j.Log4j2;
import org.victorpiles.escacs.engine.Board;
import org.victorpiles.escacs.engine.piece.Piece;
import org.victorpiles.escacs.engine.piece.alliance.PieceAlliance;
import org.victorpiles.escacs.engine.square.Square;
import org.victorpiles.escacs.engine.util.board.BoardUtils;
import org.victorpiles.escacs.engine.util.parser.MoveParser;

import java.util.ArrayList;
import java.util.List;

@Log4j2
public class Knight extends Piece {

    public Knight(PieceType type, PieceAlliance alliance, int position) {
        super(type, alliance, position);
    }

    @Override
    public List<String> getValidMoveValues(Board board) {
        int[] offsets = {-17, -15, -10, -6, 6, 10, 15, 17};
        List<String> validMoveValues = new ArrayList<>();

        for (int offset : offsets) {
            int destination = position + offset;

            /* Casella fora dels límits del tauler -> moviment no vàlid */
            if (!Square.isValidPosition(destination)) {
                continue;
            }
            if (BoardUtils.FIRST_COLUMN.get(position) && ((offset == -17) || (offset == -10) || (offset == 6) || (offset == 15))) {
                continue;
            }
            if (BoardUtils.SECOND_COLUMN.get(position) && ((offset == -10) || (offset == 6))) {
                continue;
            }
            if (BoardUtils.SEVENTH_COLUMN.get(position) && ((offset == -6) || (offset == 10))) {
                continue;
            }
            if (BoardUtils.EIGHT_COLUMN.get(position) && ((offset == -15) || (offset == -6) || (offset == 10) || (offset == 17))) {
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
