package org.victorpiles.escacs.engine.util.parser;

import org.jetbrains.annotations.NotNull;
import org.victorpiles.escacs.engine.piece.Piece;
import org.victorpiles.escacs.engine.util.notation.NotationConverter;

public abstract class MoveParser {

    public static int parsePieceOriginPosition(@NotNull String moveValue) {
        String piecePositionNotation = String.valueOf(moveValue.charAt(2)) + moveValue.charAt(3);
        return NotationConverter.notationToIndex(piecePositionNotation);
    }

    public static int parsePieceDestinationPosition(@NotNull String moveValue) {
        String piecePositionNotation = String.valueOf(moveValue.charAt(4)) + moveValue.charAt(5);
        return NotationConverter.notationToIndex(piecePositionNotation);
    }

    public static @NotNull String toMoveNotation(@NotNull Piece piece, int originPosition, int destinationPosition) {
        String pieceType = piece.getType().getNotation();
        String pieceAlliance = piece.getAlliance().getNotation();
        String originPositionNotation = NotationConverter.indexToNotation(originPosition);
        String destinationPositionNotation = NotationConverter.indexToNotation(destinationPosition);

        return pieceType + pieceAlliance + originPositionNotation + destinationPositionNotation;
    }
}