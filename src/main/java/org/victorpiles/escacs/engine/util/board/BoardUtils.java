package org.victorpiles.escacs.engine.util.board;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public abstract class BoardUtils {

    private static final int NUMBER_OF_SQUARES = 64;
    private static final int NUMBER_OF_SQUARES_PER_ROW = 8;

    public static final List<Boolean> FIRST_COLUMN = initializeColumn(0);
    public static final List<Boolean> SECOND_COLUMN = initializeColumn(1);
    public static final List<Boolean> SEVENTH_COLUMN = initializeColumn(6);
    public static final List<Boolean> EIGHT_COLUMN = initializeColumn(7);

    public static final List<Boolean> SECOND_RANK = initializeRow(48);
    public static final List<Boolean> SEVENTH_RANK = initializeRow(8);

    private static @NotNull List<Boolean> initializeColumn(int columnNumber) {
        final Boolean[] column = new Boolean[NUMBER_OF_SQUARES];
        Arrays.fill(column, false);
        do {
            column[columnNumber] = true;
            columnNumber += NUMBER_OF_SQUARES_PER_ROW;
        } while (columnNumber < NUMBER_OF_SQUARES);
        return List.of((column));
    }

    private static @NotNull List<Boolean> initializeRow(int rowNumber) {
        final Boolean[] row = new Boolean[NUMBER_OF_SQUARES];
        Arrays.fill(row, false);
        do {
            row[rowNumber] = true;
            rowNumber++;
        } while (rowNumber % NUMBER_OF_SQUARES_PER_ROW != 0);
        return List.of(row);
    }
}
