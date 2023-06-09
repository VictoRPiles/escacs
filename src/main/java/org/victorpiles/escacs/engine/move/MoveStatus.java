package org.victorpiles.escacs.engine.move;

public enum MoveStatus {
    OK {
        @Override
        public boolean ok() {
            return true;
        }
    },
    KO {
        @Override
        public boolean ok() {
            return false;
        }
    },
    CHECK {
        @Override
        public boolean ok() {
            return false;
        }
    },
    CHECK_MATE {
        @Override
        public boolean ok() {
            return false;
        }
    };

    public abstract boolean ok();
}
