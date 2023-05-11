package org.victorpiles.escacs.engine.piece.type;

import org.victorpiles.escacs.engine.Board;
import org.victorpiles.escacs.engine.piece.Piece;
import org.victorpiles.escacs.engine.piece.alliance.PieceAlliance;
import org.victorpiles.escacs.engine.square.Square;
import org.victorpiles.escacs.engine.util.board.BoardUtils;
import org.victorpiles.escacs.engine.util.parser.MoveParser;

import java.util.ArrayList;
import java.util.List;

public class Pawn extends Piece {

    public Pawn(PieceType type, PieceAlliance alliance, int position) {
        super(type, alliance, position);
    }

    @Override
    public List<String> getValidMoveValues(Board board) {
        /* Els peons només poden avançar en una direcció, que varia segons l'aliança */
        int[] offsets = {7 * alliance.direction(), 8 * alliance.direction(), 9 * alliance.direction(), 16 * alliance.direction()};
        List<String> validMoveValues = new ArrayList<>();

        for (int offset : offsets) {
            int destination = position + offset;

            /* Casella fora dels límits del tauler -> moviment no vàlid */
            if (!Square.isValidPosition(destination)) {
                continue;
            }

            /* Casella de destí ocupada en moviment de desplaçament -> moviment no vàlid  */
            Square destinationSquare = board.getSquareList().get(destination);
            if ((offset == (8 * alliance.direction()) || offset == (16 * alliance.direction())) && destinationSquare.isOccupied()) {
                continue;
            }

            /* Moviment doble */
            if (offset == (16 * alliance.direction())) {
                /* Si és blanc i està a la segona fila -> primer moviment */
                boolean lightAndSecondRank = alliance.isLight() && BoardUtils.SECOND_RANK.get(position);
                /* Si és negre i està a la sèptima fila -> primer moviment */
                boolean darkAndSeventhRank = !alliance.isLight() && BoardUtils.SEVENTH_RANK.get(position);


                /* Si no és el primer moviment -> moviment no vàlid */
                if (!(lightAndSecondRank || darkAndSeventhRank)) {
                    continue;
                }

                /* Casella entre el peó i el destí ocupada -> moviment no vàlid */
                int behindDestination = position + (8 * alliance.direction());
                Square squareBehindDestination = board.getSquareList().get(behindDestination);
                if (squareBehindDestination.isOccupied()) {
                    continue;
                }

                validMoveValues.add(MoveParser.toMoveNotation(this, this.position, destination));
            }

            /* Atac a la dreta */
            if (offset == (7 * alliance.direction())) {
                boolean firstColumnAndDark = BoardUtils.FIRST_COLUMN.get(position) && !alliance.isLight();
                boolean eightColumnAndLight = BoardUtils.EIGHT_COLUMN.get(position) && alliance.isLight();

                /* Casella fora dels límits del tauler -> moviment no vàlid */
                if (firstColumnAndDark || eightColumnAndLight) {
                    continue;
                }

                /* Casella no ocupada -> moviment no vàlid */
                if (destinationSquare.isEmpty()) {
                    continue;
                }

                Piece attackedPiece = destinationSquare.getPiece();

                /* Casella ocupada amb peça amiga -> moviment no vàlid */
                if (alliance.equals(attackedPiece.getAlliance())) {
                    continue;
                }
            }

            /* Atac a l'esquerra */
            if (offset == (9 * alliance.direction())) {
                boolean firstColumnAndLight = BoardUtils.FIRST_COLUMN.get(position) && alliance.isLight();
                boolean eightColumnAndDark = BoardUtils.EIGHT_COLUMN.get(position) && !alliance.isLight();

                /* Casella fora dels límits del tauler -> moviment no vàlid */
                if (firstColumnAndLight || eightColumnAndDark) {
                    continue;
                }

                /* Casella no ocupada -> moviment no vàlid */
                if (destinationSquare.isEmpty()) {
                    continue;
                }

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
