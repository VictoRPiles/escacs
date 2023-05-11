package org.victorpiles.escacs.engine.piece.type;

import org.victorpiles.escacs.engine.Board;
import org.victorpiles.escacs.engine.piece.Piece;
import org.victorpiles.escacs.engine.piece.alliance.PieceAlliance;
import org.victorpiles.escacs.engine.square.Square;
import org.victorpiles.escacs.engine.util.board.BoardUtils;
import org.victorpiles.escacs.engine.util.parser.MoveParser;

import java.util.ArrayList;
import java.util.List;

public class Bishop extends Piece {

    public Bishop(PieceType type, PieceAlliance alliance, int position) {
        super(type, alliance, position);
    }

    @Override
    public List<String> getValidMoveValues(Board board) {
        int[] vectors = {-9, -7, 7, 9};
        List<String> validMoveValues = new ArrayList<>();

        for (int vector : vectors) {
            int destination = position;

            /* Moviments en la direcció del vector */
            while (Square.isValidPosition(destination)) {
                /* Casella fora dels límits del tauler -> no seguir en la direcció del vector */
                if (BoardUtils.FIRST_COLUMN.get(destination) && ((vector == -9) || (vector == 7))) {
                    break;
                }
                if (BoardUtils.EIGHT_COLUMN.get(destination) && ((vector == -7) || (vector == 9))) {
                    break;
                }

                destination += vector;

                if (!Square.isValidPosition(destination)) {
                    break;
                }

                Square destinationSquare = board.getSquareList().get(destination);
                if (destinationSquare.isOccupied()) {
                    Piece attackedPiece = destinationSquare.getPiece();

                    /* Casella ocupada amb peça amiga -> no seguir en la direcció del vector */
                    if (alliance.equals(attackedPiece.getAlliance())) {
                        break;
                    }

                    /* Casella ocupada amb peça enemiga -> afegir moviment i no seguir en la direcció del vector */
                    validMoveValues.add(MoveParser.toMoveNotation(this, this.position, destination));
                    break;
                }

                /* Casella buida -> seguir en la direcció del vector */
                validMoveValues.add(MoveParser.toMoveNotation(this, this.position, destination));
            }
        }

        return validMoveValues;
    }
}
