package org.victorpiles.escacs.engine.util.parser;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.victorpiles.escacs.engine.exception.ContextParsingException;
import org.victorpiles.escacs.engine.piece.Piece;
import org.victorpiles.escacs.engine.piece.alliance.PieceAlliance;
import org.victorpiles.escacs.engine.piece.type.*;
import org.victorpiles.escacs.engine.util.notation.NotationConverter;

public class PieceParser {

    public static @NotNull Piece parsePiece(@NotNull String pieceNotation) {
        PieceType pieceType = parsePieceType(pieceNotation);
        PieceAlliance pieceAlliance = parsePieceAlliance(pieceNotation);
        int piecePosition = parsePiecePosition(pieceNotation);

        /* No és una casella amb informació vàlida */
        if ((pieceType == null) || (pieceAlliance == null) || ((piecePosition < 0) || (piecePosition >= 64))) {
            throw new ContextParsingException("Invalid square information " + pieceNotation);
        }

        Piece piece;
        switch (pieceType) {
            case ROOK -> piece = new Rook(pieceType, pieceAlliance, piecePosition);
            case BISHOP -> piece = new Bishop(pieceType, pieceAlliance, piecePosition);
            case KNIGHT -> piece = new Knight(pieceType, pieceAlliance, piecePosition);
            case KING -> piece = new King(pieceType, pieceAlliance, piecePosition);
            case QUEEN -> piece = new Queen(pieceType, pieceAlliance, piecePosition);
            case PAWN -> piece = new Pawn(pieceType, pieceAlliance, piecePosition);
            default -> throw new IllegalStateException("Unexpected value: " + pieceType);
        }
        return piece;
    }

    private static @Nullable PieceType parsePieceType(@NotNull String pieceNotation) {
        String pieceTypeNotation = String.valueOf(pieceNotation.charAt(0));
        for (PieceType pieceTypeValue : PieceType.values()) {
            if (pieceTypeValue.getNotation().equals(pieceTypeNotation)) {
                return pieceTypeValue;
            }
        }
        return null;
    }

    private static @Nullable PieceAlliance parsePieceAlliance(@NotNull String pieceNotation) {
        String pieceAllianceNotation = String.valueOf(pieceNotation.charAt(1));
        for (PieceAlliance pieceAllianceValue : PieceAlliance.values()) {
            if (pieceAllianceValue.getNotation().equals(pieceAllianceNotation)) {
                return pieceAllianceValue;
            }
        }
        return null;
    }

    private static int parsePiecePosition(@NotNull String squareInfo) {
        String piecePositionNotation = String.valueOf(squareInfo.charAt(2)) + squareInfo.charAt(3);
        return NotationConverter.notationToIndex(piecePositionNotation);
    }
}
