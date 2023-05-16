package org.victorpiles.escacs.engine.util.notation;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class NotationConverter {

    public static int notationToIndex(@NotNull String notation) {
        char fileChar = notation.charAt(0);
        int file = fileChar - 'a';
        int rank = 8 - (notation.charAt(1) - '0');
        return rank * 8 + file;
    }

    public static @Nullable String indexToNotation(int index) {
        if (index < 0 || index > 63) {
            return null;
        }
        char fileChar = (char) ('a' + (index % 8));
        int rank = 8 - (index / 8);
        return String.valueOf(fileChar) + rank;
    }
}