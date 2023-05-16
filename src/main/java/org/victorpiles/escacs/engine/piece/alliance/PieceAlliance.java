package org.victorpiles.escacs.engine.piece.alliance;

import org.jetbrains.annotations.NotNull;
import org.victorpiles.escacs.engine.piece.notation.NotationEnum;

public enum PieceAlliance implements NotationEnum {
    LIGHT {
        @Override
        public boolean isLight() {
            return true;
        }

        @Override
        public int direction() {
            return -1;
        }

        @Override
        public @NotNull PieceAlliance opponent() {
            return DARK;
        }

        @Override
        public @NotNull String getNotation() {
            return "l";
        }
    },
    DARK {
        @Override
        public boolean isLight() {
            return false;
        }

        @Override
        public int direction() {
            return 1;
        }

        @Override
        public @NotNull PieceAlliance opponent() {
            return LIGHT;
        }

        @Override
        public @NotNull String getNotation() {
            return "d";
        }
    };

    public abstract boolean isLight();

    public abstract int direction();

    public abstract PieceAlliance opponent();
}
