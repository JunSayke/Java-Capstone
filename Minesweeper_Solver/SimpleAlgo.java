package Minesweeper_Solver;

public class SimpleAlgo {
    private State[][] field;
    private int fieldRows;
    private int fieldCols;

    public SimpleAlgo(State[][] field) {
        this.field = field;
        fieldRows = field.length;
        fieldCols = field[0].length;
    }

    /**
     * Solves a single field the easy way ..
     * if the number of blocks around equals the number on this block, flag them otherwise open them
     *
     * @param x why are you reading this?
     * @param y you seriously should understand it
     */
    /*private void solveSingle(int x, int y) {
        int countClosed = getSurroundingByType(x, y, State.BLOCK_CLOSED);
        if (countClosed == 0) return;

        int countAlreadyFlagged = getSurroundingByType(x, y, State.BLOCK_FLAG);
        int countMinesAround = field[x][y].getValue();

        // First: flag as much as we can
        if (countMinesAround == countClosed + countAlreadyFlagged) {
            System.out.println("  Flag: " + field[x][y].getValue() + " at (" + (x + 1) + "/" + (y + 1) + ")");
            board.flagSurrounding(x, y);
            countAlreadyFlagged = getSurroundingByType(x, y, State.BLOCK_FLAG);
        }

        // Second: open the ones around
        if (countMinesAround == countAlreadyFlagged) {
            System.out.println("  Open: " + field[x][y].getValue() + " at (" + (x + 1) + "/" + (y + 1) + ")");
            board.openSurrounding(x, y);
        }

    }*/

    /**
     * Discovers all the fields around that match the parameter
     *
     * @param x    why are you reading this?
     * @param y    you seriously should understand it
     * @param type type to compare
     * @return the amount of fields around that match type
     */
    private int getSurroundingByType(int x, int y, State type) {
        int hits = 0;

        if (y > 0) {
            if (x > 0 && field[x - 1][y - 1] == type) hits++; // top ■□□
            if (field[x][y - 1] == type) hits++;   // top □■□
            if (x < fieldCols - 1 && field[x + 1][y - 1] == type) hits++; // top □□■
        }

        if (x > 0 && field[x - 1][y] == type) hits++; // middle ■□□
        if (x < fieldCols - 1 && field[x + 1][y] == type) hits++; // middle □□■

        if (y < fieldRows - 1) {
            if (x > 0 && field[x - 1][y + 1] == type) hits++; // bottom ■□□
            if (field[x][y + 1] == type) hits++;   // bottom □■□
            if (x < fieldCols - 1 && field[x + 1][y + 1] == type) hits++; // bottom □□■
        }

        return hits;
    }

    /**
     * Discovers all boundary blocks around
     * A boundary block is an unopened block with opened blocks next to it.
     *
     * @param x why are you reading this?
     * @param y you seriously should understand it
     * @return true if it is a boundary block
     */
    private boolean isBoundary(int x, int y) {
        if (field[x][y] != State.BLOCK_CLOSED) return false;

        if (y > 0) {
            if (x > 0 && field[x - 1][y - 1].getValue() >= 0) return true; // top ■□□
            if (field[x][y - 1].getValue() >= 0) return true;   // top □■□
            if (x < fieldCols - 1 && field[x + 1][y - 1].getValue() >= 0) return true; // top □□■
        }

        if (x > 0 && field[x - 1][y].getValue() >= 0) return true; // middle ■□□
        if (x < fieldCols - 1 && field[x + 1][y].getValue() >= 0) return true; // middle □□■

        if (y < fieldRows - 1) {
            if (x > 0 && field[x - 1][y + 1].getValue() >= 0) return true; // bottom ■□□
            if (field[x][y + 1].getValue() >= 0) return true;   // bottom □■□
            if (x < fieldCols - 1 && field[x + 1][y + 1].getValue() >= 0) return true; // bottom □□■
        }

        return false;
    }

    /**
     * Checks if we already won the game
     *
     * @return true if we won
     */
    private boolean checkSolved() {
        for (int x = 0; x < fieldCols; x++) {
            for (int y = 0; y < fieldRows; y++) {

                if (field[x][y] == State.BLOCK_CLOSED) return false;

            }
        }

        return true;
    }

    /**
     * How many flags exist around this block?
     *
     * @param array the array to check in
     * @param x     why are you reading this?
     * @param y     you seriously should understand it
     * @return amount of flags around
     */
    private int countFlagsAround(boolean[][] array, int x, int y) {
        int mines = 0;

        if (y > 0) {
            if (x > 0 && array[x - 1][y - 1]) mines++; // top ■□□
            if (array[x][y - 1]) mines++;   // top □■□
            if (x < array.length - 1 && array[x + 1][y - 1]) mines++; // top □□■
        }

        if (x > 0 && array[x - 1][y]) mines++; // middle ■□□
        if (x < array.length - 1 && array[x + 1][y]) mines++; // middle □□■

        if (y < array[0].length - 1) {
            if (x > 0 && array[x - 1][y + 1]) mines++; // bottom ■□□
            if (array[x][y + 1]) mines++;   // bottom □■□
            if (x < array.length - 1 && array[x + 1][y + 1]) mines++; // bottom □□■
        }

        return mines;
    }
}
