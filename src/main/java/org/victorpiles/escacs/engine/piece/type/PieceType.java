package org.victorpiles.escacs.engine.piece.type;

import org.jetbrains.annotations.NotNull;
import org.victorpiles.escacs.engine.piece.notation.NotationEnum;

public enum PieceType implements NotationEnum {
    ROOK {
        @Override
        public boolean isKing() {
            return false;
        }

        @Override
        public @NotNull String getNotation() {
            return "r";
        }
    },
    BISHOP {
        @Override
        public boolean isKing() {
            return false;
        }

        @Override
        public @NotNull String getNotation() {
            return "b";
        }
    },
    KNIGHT {
        @Override
        public boolean isKing() {
            return false;
        }

        @Override
        public @NotNull String getNotation() {
            return "n";
        }
    },
    KING {
        @Override
        public boolean isKing() {
            return true;
        }

        @Override
        public @NotNull String getNotation() {
            return "k";
        }
    },
    QUEEN {
        @Override
        public boolean isKing() {
            return false;
        }

        @Override
        public @NotNull String getNotation() {
            return "q";
        }
    },
    PAWN {
        @Override
        public boolean isKing() {
            return false;
        }

        @Override
        public @NotNull String getNotation() {
            return "p";
        }
    };

    public abstract boolean isKing();
}
