package org.victorpiles.escacs.engine.piece.type;

import org.victorpiles.escacs.engine.piece.notation.NotationEnum;

public enum PieceType implements NotationEnum {
    ROOK {
        @Override
        public String getNotation() {
            return "r";
        }
    },
    BISHOP {
        @Override
        public String getNotation() {
            return "b";
        }
    },
    KNIGHT {
        @Override
        public String getNotation() {
            return "n";
        }
    },
    KING {
        @Override
        public String getNotation() {
            return "k";
        }
    },
    QUEEN {
        @Override
        public String getNotation() {
            return "q";
        }
    },
    PAWN {
        @Override
        public String getNotation() {
            return "p";
        }
    };
}
