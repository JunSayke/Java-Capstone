package Minesweeper_Solver;

public enum Block {
    MINE_EXPLODED(-3),
    CLOSED(-2),
    FLAG(-1),
    EMPTY(0),
    ONE(1),
    TWO(2),
    THREE(3),
    FOUR(4),
    FIVE(5),
    SIX(6),
    SEVEN(7),
    EIGHT(8);

    private final int value;

    Block(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}