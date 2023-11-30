package Minesweeper_Solver;

public enum State {
    BLOCK_MINE_EXPLODED(-3),
    BLOCK_CLOSED(-2),
    BLOCK_FLAG(-1),
    BLOCK_EMPTY(0),
    BLOCK_ONE(1),
    BLOCK_TWO(2),
    BLOCK_THREE(3),
    BLOCK_FOUR(4),
    BLOCK_FIVE(5),
    BLOCK_SIX(6),
    BLOCK_SEVEN(7),
    BLOCK_EIGHT(8);

    private final int value;

    State(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}