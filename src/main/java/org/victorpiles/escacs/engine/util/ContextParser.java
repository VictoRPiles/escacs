package org.victorpiles.escacs.engine.util;

import org.victorpiles.escacs.engine.Board;
import org.victorpiles.escacs.engine.exception.ContextParsingException;
import org.victorpiles.escacs.engine.piece.Piece;
import org.victorpiles.escacs.engine.piece.alliance.PieceAlliance;
import org.victorpiles.escacs.engine.piece.type.*;

public abstract class ContextParser {

    public static Board parse(String context) {
        Board board = Board.build();

        String[] squareInContext = parseSquares(context);

        for (String squareInfo : squareInContext) {
            PieceType pieceType = parsePieceType(squareInfo);
            PieceAlliance pieceAlliance = parsePieceAlliance(squareInfo);
            int piecePosition = parsePiecePosition(squareInfo);

            /* No és una casella amb informació vàlida */
            if ((pieceType == null) || (pieceAlliance == null) || ((piecePosition < 0) || (piecePosition >= 64))) {
                throw new ContextParsingException("Invalid square information " + squareInfo);
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
            board.addPiece(piece);
        }

        return board;
    }

    private static String[] parseSquares(String context) {
        int squareInfoLength = 4;

        int numberOfSquares = context.length() / squareInfoLength;
        String[] squares = new String[numberOfSquares];

        for (int square = 0; square < numberOfSquares; square++) {
            int startOfSquareInfo = square * squareInfoLength;
            int endOfSquareInfo = ((square + 1) * squareInfoLength);
            String squareInfo = context.substring(startOfSquareInfo, endOfSquareInfo);

            squares[square] = squareInfo;
        }

        return squares;
    }

    private static PieceType parsePieceType(String squareInfo) {
        String pieceTypeNotation = String.valueOf(squareInfo.charAt(0));
        for (PieceType pieceTypeValue : PieceType.values()) {
            if (pieceTypeValue.getNotation().equals(pieceTypeNotation)) {
                return pieceTypeValue;
            }
        }
        return null;
    }

    private static PieceAlliance parsePieceAlliance(String squareInfo) {
        String pieceAllianceNotation = String.valueOf(squareInfo.charAt(1));
        for (PieceAlliance pieceAllianceValue : PieceAlliance.values()) {
            if (pieceAllianceValue.getNotation().equals(pieceAllianceNotation)) {
                return pieceAllianceValue;
            }
        }
        return null;
    }

    private static int parsePiecePosition(String squareInfo) {
        String piecePositionNotation = String.valueOf(squareInfo.charAt(2)) + squareInfo.charAt(3);
        return NotationConverter.notationToIndex(piecePositionNotation);
    }
}
