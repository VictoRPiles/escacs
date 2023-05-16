package org.victorpiles.escacs.engine;

import org.junit.jupiter.api.Test;
import org.victorpiles.escacs.engine.move.MoveStatus;
import org.victorpiles.escacs.engine.piece.alliance.PieceAlliance;
import org.victorpiles.escacs.engine.piece.type.King;
import org.victorpiles.escacs.engine.piece.type.PieceType;
import org.victorpiles.escacs.engine.util.parser.ContextParser;

import java.util.List;

class BoardTest {

    @Test
    void isKingInCheckMate() {
        String piece = "qda1";
        String move = "qda1c1";
        String context = "rda8bdc8ndd8kde8rdf8pdb7rld7pdf7pda6pde6pdg6pdh6pdc5nlg5rle4plg4ple3plf3ndg3bdb2plc2qld2qda1blc1kle1blf1";
        List<String> validMoveValues = Engine.getValidMoveValues(piece, context);
        System.out.println(validMoveValues);
        Board board = ContextParser.parse(context);
        MoveStatus status = board.execute(move, true);
        System.out.println(status);
    }

    @Test
    void copyOf() {
        Board original = Board.build();
        Board copy = Board.copyOf(original);

        System.out.println("ORIGINAL");
        System.out.println(original);
        System.out.println("COPY");
        System.out.println(copy);

        original.addPiece(new King(PieceType.KING, PieceAlliance.DARK, 32));

        System.out.println("ORIGINAL");
        System.out.println(original);
        System.out.println("COPY");
        System.out.println(copy);
    }
}