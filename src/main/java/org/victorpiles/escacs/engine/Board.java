package org.victorpiles.escacs.engine;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.victorpiles.escacs.engine.move.MoveStatus;
import org.victorpiles.escacs.engine.piece.Piece;
import org.victorpiles.escacs.engine.piece.alliance.PieceAlliance;
import org.victorpiles.escacs.engine.piece.type.King;
import org.victorpiles.escacs.engine.square.Square;
import org.victorpiles.escacs.engine.util.parser.MoveParser;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Log4j2
public class Board {

    private final List<Square> squareList;

    public Board() {
        squareList = new ArrayList<>();
    }

    public Board(List<Square> squareList) {
        this.squareList = squareList;
    }

    public void addPiece(@NotNull Piece piece) {
        squareList.get(piece.getPosition()).setPiece(piece);
    }

    public void removePiece(@NotNull Piece piece) {
        squareList.get(piece.getPosition()).setPiece(null);
    }

    private void movePiece(@NotNull Piece movedPiece, @NotNull String moveValue) {
        this.removePiece(movedPiece);
        movedPiece.setPosition(MoveParser.parsePieceDestinationPosition(moveValue));
        this.addPiece(movedPiece);
    }

    public @Nullable King findKing(PieceAlliance alliance) {
        // TODO: 11/5/23 orElseThrow en findKing
        return (King) squareList
                .stream()
                .filter(Square::isOccupied)
                .map(Square::getPiece)
                .filter(piece -> piece.getAlliance().equals(alliance))
                .filter(piece -> piece.getType().isKing()).findFirst()
                .orElse(null);
    }

    public @NotNull List<Piece> getPiecesOfAlliance(PieceAlliance alliance) {
        return squareList
                .stream()
                .filter(Square::isOccupied)
                .map(Square::getPiece)
                .filter(piece -> piece.getAlliance().equals(alliance))
                .collect(Collectors.toList());
    }

    public boolean isKingInCheck(@NotNull PieceAlliance alliance) {
        King king = findKing(alliance);
        List<Piece> opponentPieces = getPiecesOfAlliance(alliance.opponent());

        for (Piece piece : opponentPieces) {
            if (!piece.hasValidMoves(this)) {
                continue;
            }

            List<String> pieceValidMoves = piece.getValidMoveValues(this);
            for (String move : pieceValidMoves) {
                int destinationPosition = MoveParser.parsePieceDestinationPosition(move);
                if (destinationPosition == king.getPosition()) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean isKingInCheckMate(@NotNull PieceAlliance alliance) {
        if (!isKingInCheck(alliance)) {
            return false;
        }
        List<Piece> pieces = getPiecesOfAlliance(alliance);
        for (Piece piece : pieces) {
            if (!piece.hasValidMoves(this)) {
                continue;
            }

            List<String> pieceValidMoves = piece.getValidMoveValues(this);
            for (String move : pieceValidMoves) {
                MoveStatus status = execute(move, false);
                if (status.ok()) {
                    return false;
                }
            }
        }

        return true;
    }

    public static @NotNull Board build() {
        ArrayList<Square> squares = new ArrayList<>();
        for (int i = 0; i < 64; i++) {
            squares.add(new Square());
        }

        return new Board(squares);
    }

    public @NotNull MoveStatus execute(@NotNull String move, boolean evaluateCheckMate) {
        int origin = MoveParser.parsePieceOriginPosition(move);
        Piece movedPiece = squareList.get(origin).getPiece();

        List<String> validMoveValues = movedPiece.getValidMoveValues(this);
        if (!validMoveValues.contains(move)) {
            return MoveStatus.KO;
        }

        /* Executa el moviment a un tauler paral·lel i processa el resultat */
        Board clonedBoard = Board.copyOf(this);
        clonedBoard.movePiece(movedPiece, move);
        PieceAlliance alliance = movedPiece.getAlliance();
        if (clonedBoard.isKingInCheck(alliance)) {
            return MoveStatus.KO;
        }
        if (evaluateCheckMate && clonedBoard.isKingInCheckMate(alliance.opponent())) {
            return MoveStatus.CHECK_MATE;
        }
        if (clonedBoard.isKingInCheck(alliance.opponent())) {
            return MoveStatus.CHECK;
        }

        return MoveStatus.OK;
    }

    @Override
    public @NotNull String toString() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < squareList.size(); i++) {
            if ((i != 0) && (i % 8 == 0)) {
                sb.append("\n");
            }

            sb.append(squareList.get(i)).append("  ");
        }

        return sb.toString();
    }

    public static @NotNull Board copyOf(@NotNull Board board) {
        Board clone = new Board();
        board.squareList.forEach(square -> clone.squareList.add(new Square(square.getPiece())));
        return clone;
    }
}
