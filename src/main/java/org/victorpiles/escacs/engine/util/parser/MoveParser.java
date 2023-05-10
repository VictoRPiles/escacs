package org.victorpiles.escacs.engine.util.parser;

import org.victorpiles.escacs.api.move.Move;
import org.victorpiles.escacs.engine.piece.Piece;
import org.victorpiles.escacs.engine.util.notation.NotationConverter;

public abstract class MoveParser {

    public static int parsePieceOriginPosition(Move move) {
        String moveValue = move.getValue();

        String piecePositionNotation = String.valueOf(moveValue.charAt(2)) + moveValue.charAt(3);
        return NotationConverter.notationToIndex(piecePositionNotation);
    }

    public static int parsePieceDestinationPosition(Move move) {
        String moveValue = move.getValue();

        String piecePositionNotation = String.valueOf(moveValue.charAt(4)) + moveValue.charAt(5);
        return NotationConverter.notationToIndex(piecePositionNotation);
    }

    public static String toMoveNotation(Piece piece, int originPosition, int destinationPosition) {
        String pieceType = piece.getType().getNotation();
        String pieceAlliance = piece.getAlliance().getNotation();
        String originPositionNotation = NotationConverter.indexToNotation(originPosition);
        String destinationPositionNotation = NotationConverter.indexToNotation(destinationPosition);

        return pieceType + pieceAlliance + originPositionNotation + destinationPositionNotation;
    }
}