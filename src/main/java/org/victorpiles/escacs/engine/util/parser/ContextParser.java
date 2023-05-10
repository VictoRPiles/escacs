package org.victorpiles.escacs.engine.util.parser;

import org.victorpiles.escacs.engine.Board;
import org.victorpiles.escacs.engine.piece.Piece;

public abstract class ContextParser {

    public static Board parse(String context) {
        Board board = Board.build();

        String[] squareInContext = parseSquares(context);

        for (String squareInfo : squareInContext) {
            Piece piece = PieceParser.parsePiece(squareInfo);
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
}
