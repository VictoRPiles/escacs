package org.victorpiles.escacs.engine.piece.alliance;

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
        public String getNotation() {
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
        public String getNotation() {
            return "d";
        }
    };

    public abstract boolean isLight();

    public abstract int direction();
}
